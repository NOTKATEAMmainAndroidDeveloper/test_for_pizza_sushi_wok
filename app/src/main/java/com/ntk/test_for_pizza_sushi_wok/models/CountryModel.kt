package com.ntk.test_for_pizza_sushi_wok.models

import com.google.gson.annotations.SerializedName

data class CountryModel(
    @SerializedName("id")var id: String = "",
    @SerializedName("name")var name: String = "",
)
