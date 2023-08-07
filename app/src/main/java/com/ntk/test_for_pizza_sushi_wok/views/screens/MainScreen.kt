package com.ntk.test_for_pizza_sushi_wok.views.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ntk.test_for_pizza_sushi_wok.enums.CarsFilterEnum
import com.ntk.test_for_pizza_sushi_wok.models.CarModel
import com.ntk.test_for_pizza_sushi_wok.viewmodels.MainViewModel
import com.ntk.test_for_pizza_sushi_wok.views.custom.CarModelView
import com.ntk.test_for_pizza_sushi_wok.views.dialogs.CarDialog

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(controller: NavHostController) {
    val viewModel by remember { mutableStateOf(MainViewModel()) }

    var refreshing by remember { mutableStateOf(false) }

    val selectedModel  = remember { mutableStateOf(CarModel()) }

    val isProdCountryDropOpen  = remember { mutableStateOf(false) }
    val isBrandDropOpen  = remember { mutableStateOf(false) }
    val isSortDropOpen  = remember { mutableStateOf(false) }


    val openAddDialog = remember { mutableStateOf(false) }
    if(openAddDialog.value)CarDialog(openAddDialog, viewModel.brandList){newModel ->
        viewModel.addCar(newModel)
        openAddDialog.value = false
    }

    val openEditDialog = remember { mutableStateOf(false) }
    if(openEditDialog.value)CarDialog(openEditDialog, viewModel.brandList, selectedModel.value){newModel ->
        viewModel.editCar(newModel)
        openEditDialog.value = false
    }

    LaunchedEffect(refreshing) {
        if (refreshing) {
            viewModel.getAllCars {
                refreshing = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = Alignment.BottomEnd
    ){
        FloatingActionButton(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    spotColor = Color.Gray,
                    ambientColor = Color.Gray,
                    shape = RoundedCornerShape(16.dp)
                ),
            containerColor = Color.Black,
            onClick = {
                openAddDialog.value = true
            }
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = rememberSwipeRefreshState(isRefreshing = refreshing),
            onRefresh = { refreshing = true },
        ) {
            Column(modifier = Modifier.fillMaxSize()) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                isProdCountryDropOpen.value = true
                            },
                        style = TextStyle(color = Color.White),
                        text = "Производитель: ${
                            if (viewModel.selectedCountry.value == -1)
                                "все" else viewModel.countryList.first { it.id.toInt() == viewModel.selectedCountry.value }.name
                        }"
                    )

                    DropdownMenu(
                        expanded = isProdCountryDropOpen.value,
                        onDismissRequest = { isProdCountryDropOpen.value = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Все") },
                            onClick = {
                                viewModel.selectedCountry.value = -1
                                viewModel.selectedBrand.value = -1

                                isProdCountryDropOpen.value = false
                            }
                        )

                        viewModel.countryList.forEach {
                            DropdownMenuItem(
                                text = { Text(it.name) },
                                onClick = {
                                    viewModel.selectedCountry.value = it.id.toInt()
                                    viewModel.selectedBrand.value = -1

                                    isProdCountryDropOpen.value = false
                                }
                            )
                        }
                    }

                    Text(
                        modifier = Modifier
                            .clickable {
                                isBrandDropOpen.value = true
                            },
                        style = TextStyle(color = Color.White),
                        text = "Марка: ${
                            if (viewModel.selectedBrand.value == -1)
                                "все" else viewModel.brandList.first { it.id.toInt() == viewModel.selectedBrand.value } .name
                        }"
                    )

                    DropdownMenu(
                        expanded = isBrandDropOpen.value,
                        onDismissRequest = { isBrandDropOpen.value = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Все") },
                            onClick = {
                                viewModel.selectedBrand.value = -1

                                isBrandDropOpen.value = false
                            }
                        )

                        viewModel.brandList
                            .filter {
                                if(viewModel.selectedCountry.value != -1)
                                    it.countryid.toInt() == viewModel.selectedCountry.value
                                else true
                            }
                            .forEach {
                                DropdownMenuItem(
                                    text = { Text(it.name) },
                                    onClick = {
                                        viewModel.selectedBrand.value = it.id.toInt()
                                        isBrandDropOpen.value = false
                                    }
                                )
                            }
                    }
                }

                Text(
                    modifier = Modifier
                        .clickable {
                            isSortDropOpen.value = true
                        },
                    style = TextStyle(color = Color.White),
                    text = "Сортировка: ${
                        when (viewModel.selectedSort.value) {
                            CarsFilterEnum.None -> "без сортировки"
                            CarsFilterEnum.PriceHigh -> "Цена от большей"
                            CarsFilterEnum.PriceLow -> "Цена от меньшей"
                        }
                    }"
                )

                DropdownMenu(
                    expanded = isSortDropOpen.value,
                    onDismissRequest = { isSortDropOpen.value = false }
                ) {

                    DropdownMenuItem(
                        text = { Text("без сортировки") },
                        onClick = {
                            viewModel.selectedSort.value = CarsFilterEnum.None

                            isSortDropOpen.value = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Цена от большей") },
                        onClick = {
                            viewModel.selectedSort.value = CarsFilterEnum.PriceHigh

                            isBrandDropOpen.value = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Цена от меньшей") },
                        onClick = {
                            viewModel.selectedSort.value = CarsFilterEnum.PriceLow

                            isBrandDropOpen.value = false
                        }
                    )
                }

                LazyVerticalStaggeredGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = StaggeredGridCells.Fixed(2)
                ) {
                    items(
                        viewModel.filterList()
                    ) { model ->
                        CarModelView(
                            model = model,
                            onEdit = {
                                selectedModel.value = it
                                openEditDialog.value = true
                            },
                            onDelete = {
                                selectedModel.value = it
                                viewModel.deleteCar(it)
                            }
                        )
                    }
                }
            }
        }
    }
}