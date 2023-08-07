package com.ntk.test_for_pizza_sushi_wok.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jasync.sql.db.Connection
import com.ntk.test_for_pizza_sushi_wok.MainActivity.Companion.conf
import com.ntk.test_for_pizza_sushi_wok.domain.usecase.postgree.deleteModelFromBase
import com.ntk.test_for_pizza_sushi_wok.domain.usecase.postgree.editModelFromBase
import com.ntk.test_for_pizza_sushi_wok.domain.usecase.postgree.getPostgreeConection
import com.ntk.test_for_pizza_sushi_wok.domain.usecase.postgree.proceedListToModel
import com.ntk.test_for_pizza_sushi_wok.domain.usecase.postgree.pushModelToBase
import com.ntk.test_for_pizza_sushi_wok.enums.CarsFilterEnum
import com.ntk.test_for_pizza_sushi_wok.models.BrandModel
import com.ntk.test_for_pizza_sushi_wok.models.CarModel
import com.ntk.test_for_pizza_sushi_wok.models.CountryModel
import com.ntk.test_for_pizza_sushi_wok.receivers.NetworkRecevier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {
    var carList: SnapshotStateList<CarModel> = mutableStateListOf()
    var brandList: SnapshotStateList<BrandModel> = mutableStateListOf()
    var countryList: SnapshotStateList<CountryModel> = mutableStateListOf()

    var selectedBrand = mutableStateOf(-1)
    var selectedCountry = mutableStateOf(-1)

    var isDataLoading = mutableStateOf(true)

    var selectedSort: MutableState<CarsFilterEnum> = mutableStateOf(CarsFilterEnum.None)

    private lateinit var connection: Connection

    init {
        viewModelScope.launch{
            if(NetworkRecevier.is_online.value){
                getConnection().join()

                getAllCountry().join()
                getAllBrand().join()

                getAllCars()
            }
        }
    }

    private suspend fun getConnection() = viewModelScope.launch{
        val newConnection = withContext(Dispatchers.IO) {
            getPostgreeConection(conf)?.connect()?.get()
        }

        if(newConnection != null)connection = newConnection
    }

    fun getAllCars(onCompleted: () -> Unit = {}) = viewModelScope.launch{
        if (!connection.isConnected())connection.connect()

        carList.clear()

        isDataLoading.value = true

        withContext(Dispatchers.IO) {
            connection.sendPreparedStatement(
                "SELECT * FROM CARS"
            ).get()
                .proceedListToModel<CarModel> {model ->
                    model.brand = brandList.firstOrNull{ it.id == model.brandid.toString() }

                    carList.add(model)

                    isDataLoading.value = false
            }
        }

        onCompleted()
    }

    fun addCar(newModel: CarModel) = viewModelScope.launch {
        newModel.pushModelToBase(connection)
            .invokeOnCompletion {
                getAllCars()
            }
    }

    fun editCar(editModel: CarModel) = viewModelScope.launch {
        editModel.editModelFromBase(connection)
            .invokeOnCompletion {
                getAllCars()
            }
    }

    fun deleteCar(deleteModel: CarModel) = viewModelScope.launch {
        deleteModel.deleteModelFromBase(connection)
            .invokeOnCompletion {
                carList.remove(deleteModel)
            }
    }

    private fun getAllBrand(onCompleted: () -> Unit = {}) = viewModelScope.launch {
        if (!connection.isConnected())connection.connect()

        brandList.clear()

        withContext(Dispatchers.IO) {
            connection.sendPreparedStatement(
                "SELECT * FROM BRAND"
            ).get()
                .proceedListToModel<BrandModel> {model ->
                    model.country = countryList.firstOrNull { it.id == model.countryid }

                    brandList.add(model)
                }
        }

        onCompleted()
    }

    private fun getAllCountry(onCompleted: () -> Unit = {}) = viewModelScope.launch {
        if (!connection.isConnected())connection.connect()

        countryList.clear()

        withContext(Dispatchers.IO) {
            connection.sendPreparedStatement(
                "SELECT * FROM COUNTRY_PROD"
            ).get()
                .proceedListToModel<CountryModel> {
                    countryList.add(it)
                }
        }

        onCompleted()
    }

    fun filterList(): List<CarModel>{
        if(selectedBrand.value == -1 && selectedCountry.value == -1){
            var filterList = carList.toList()

            if(selectedSort.value == CarsFilterEnum.PriceHigh){
                filterList = filterList.sortedByDescending { it.price }
            }else if (selectedSort.value == CarsFilterEnum.PriceLow){
                filterList = filterList.sortedBy { it.price }
            }

            return filterList
        }

        var filterList = carList.filter {
            if(selectedBrand.value != -1 || selectedCountry.value != -1)
                if(selectedBrand.value != -1)
                    it.brandid == selectedBrand.value
                else
                    it.brand!!.country!!.id.toInt() == selectedCountry.value
            else true
        }

        if(selectedSort.value == CarsFilterEnum.PriceHigh){
            filterList = filterList.sortedByDescending { it.price }
        }else if (selectedSort.value == CarsFilterEnum.PriceLow){
            filterList = filterList.sortedBy { it.price }
        }

        return filterList
    }

}