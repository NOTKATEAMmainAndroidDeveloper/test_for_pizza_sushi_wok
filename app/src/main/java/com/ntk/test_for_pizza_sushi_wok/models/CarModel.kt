package com.ntk.test_for_pizza_sushi_wok.models

import com.google.gson.annotations.SerializedName
import com.ntk.test_for_pizza_sushi_wok.interfaces.PostgreeModelInterface
import java.io.Serializable

data class CarModel(
    @SerializedName("id")var id: String = "",
    @SerializedName("name")var name: String = "",
    @SerializedName("max_speed")var maxSpeed: Int = 120,
    @SerializedName("force_power")var forcePower: Int = 100,
    @SerializedName("description")var description: String = "",
    @SerializedName("brandid")var brandid: Int = 1,
    @SerializedName("price")var price: Int = 1
): PostgreeModelInterface, Serializable {
    @Transient
    override var tableName: String = "cars"

    @Transient
    var brand: BrandModel? = null
}
