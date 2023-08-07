package com.ntk.test_for_pizza_sushi_wok.views.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.ntk.test_for_pizza_sushi_wok.models.BrandModel
import com.ntk.test_for_pizza_sushi_wok.models.CarModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDialog(isOpen: MutableState<Boolean>, listBrand: SnapshotStateList<BrandModel>, newCarModel: CarModel = CarModel(), onAgree: (CarModel) -> Unit){
    val nameValue = remember {
        mutableStateOf(TextFieldValue(newCarModel.name))
    }

    val maxSpeedValue = remember {
        mutableStateOf(TextFieldValue(newCarModel.maxSpeed.toString()))
    }

    val forcePowerValue = remember {
        mutableStateOf(TextFieldValue(newCarModel.forcePower.toString()))
    }

    val priceValue = remember {
        mutableStateOf(TextFieldValue(newCarModel.forcePower.toString()))
    }

    val descriptionValue = remember {
        mutableStateOf(TextFieldValue(newCarModel.description))
    }

    val brandIdValue = remember {
        mutableStateOf(newCarModel.brandid)
    }

    val isDropDownOpen = remember {
        mutableStateOf(false)
    }

    AlertDialog(
        containerColor = Color(25,25,25),
        onDismissRequest = {
            isOpen.value = false
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nameValue.value,
                    label = {
                        Text(
                            text = "Название",
                            color = if(nameValue.value.text.isEmpty()) Color.Red else Color.Gray
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White
                    ),
                    onValueChange = {
                        nameValue.value = it

                        newCarModel.name = it.text
                    }
                )

                OutlinedTextField(
                    value = maxSpeedValue.value,
                    label = {
                        Text(
                            text = "Максимальная скорость",
                            color = if(maxSpeedValue.value.text.isEmpty()) Color.Red else Color.Gray
                        )
                    },
                    onValueChange = {
                        maxSpeedValue.value = it

                        try {
                            newCarModel.maxSpeed = it.text.toInt()
                        }catch (_: Exception){}
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = forcePowerValue.value,
                    label = {
                        Text(
                            text = "Количество лошадиных сил",
                            color = if(forcePowerValue.value.text.isEmpty()) Color.Red else Color.Gray
                        )
                    },
                    onValueChange = {
                        forcePowerValue.value = it

                        try {
                            newCarModel.forcePower = it.text.toInt()
                        }catch (_: Exception){}
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = descriptionValue.value,
                    label = {
                        Text(
                            text = "Описание",
                            color = if(descriptionValue.value.text.isEmpty()) Color.Red else Color.Gray
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White
                    ),
                    onValueChange = {
                        descriptionValue.value = it

                        newCarModel.description = it.text
                    }
                )


                Text(
                    modifier = Modifier
                        .clickable {
                            isDropDownOpen.value = true
                        },
                    style = TextStyle(color = Color.White),
                    text = "Марка: ${listBrand.first{it.id.toInt() == brandIdValue.value} .name}"
                )

                DropdownMenu(
                    expanded = isDropDownOpen.value,
                    onDismissRequest = { isDropDownOpen.value = false }
                ) {
                    listBrand.forEach {
                        DropdownMenuItem(
                            text = {Text(it.name)},
                            onClick = {
                                newCarModel.brandid = it.id.toInt()
                                brandIdValue.value = it.id.toInt()
                                isDropDownOpen.value = false
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = priceValue.value,
                    label = {
                        Text(
                            text = "Цена",
                            color = if(priceValue.value.text.isEmpty()) Color.Red else Color.Gray
                        )
                    },
                    onValueChange = {
                        priceValue.value = it

                        try {
                            newCarModel.price = it.text.toInt()
                        }catch (_: Exception){}
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

            }
        },
        confirmButton = {
            Button(onClick = {
                if(
                    newCarModel.name.isNotEmpty()
                    &&
                    newCarModel.description.isNotEmpty()
                ){
                    onAgree(newCarModel)
                }
            }) {
                Text(
                    text = "Подтвердить",
                    style = TextStyle(
                        color = Color.White
                    )
                )
            }
        }
    )
}