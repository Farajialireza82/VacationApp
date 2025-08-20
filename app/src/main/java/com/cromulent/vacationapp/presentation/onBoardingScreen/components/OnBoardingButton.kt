package com.cromulent.vacationapp.presentation.onBoardingScreen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cromulent.vacationapp.R

@Composable
fun OnBoardingButton(
    modifier: Modifier = Modifier,
    text: String = "Explore",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit,
) {

    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.primary)
        ),
        onClick = onClick
    ) {

        Row {

            leadingIcon?.let {

                Icon(
                    imageVector = it,
                    contentDescription = null
                )
                Spacer(Modifier.size(10.dp))
            }

            Text(
                modifier = Modifier
                    .animateContentSize(),
                text = text,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp
            )

            trailingIcon?.let {


                Spacer(Modifier.size(10.dp))

                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
        }
    }


}

@Preview
@Composable
private fun PrevOnBoardingButton() {
    OnBoardingButton(
        onClick = {}
    )
}