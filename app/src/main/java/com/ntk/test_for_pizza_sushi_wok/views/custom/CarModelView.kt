package com.ntk.test_for_pizza_sushi_wok.views.custom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ntk.test_for_pizza_sushi_wok.models.CarModel

@Composable
fun CarModelView(model: CarModel, onEdit: (CarModel) -> Unit = {}, onDelete: (CarModel) -> Unit = {}){
    val cardShape = 16.dp

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(0.45f)
            .shadow(
                elevation = 10.dp,
                ambientColor = Color.White,
                shape = RoundedCornerShape(cardShape)
            ),
        shape = RoundedCornerShape(cardShape),
        colors = CardDefaults.cardColors(
            containerColor = Color(15,15,15)
        )
    ){
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = model.name,
                style = TextStyle(
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Text(
                    text = "${model.forcePower}л.с",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White
                    )
                )

                Text(
                    text = "${model.maxSpeed}км/ч",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White
                    )
                )
            }

            Spacer(Modifier.height(8.dp))

            model.brand?.let {
                Text(
                    text = it.name,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White
                    )
                )

                it.country?.let { it1 ->
                    Text(
                        text = it1.name,
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    )
                }

            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = model.description,
                style = TextStyle(
                    fontSize = 11.sp,
                    color = Color.White
                )
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = model.price.toString() + "руб",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.White
                )
            )

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedIconButton(
                    border = BorderStroke(1.dp, Color.Blue),
                    onClick = {
                        onEdit(model)
                    }
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.Blue
                    )
                }

                OutlinedIconButton(
                    border = BorderStroke(1.dp, Color.Red),
                    onClick = {
                        onDelete(model)
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }

        }
    }
}