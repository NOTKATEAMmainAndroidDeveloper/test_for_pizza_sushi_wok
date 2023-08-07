package com.ntk.test_for_pizza_sushi_wok.models

import com.google.gson.annotations.SerializedName

data class BrandModel(
    @SerializedName("id")var id: String = "",
    @SerializedName("name")var name: String = "",
    @SerializedName("countryid")var countryid: String = ""
){
    @Transient
    var country: CountryModel? = null
}
