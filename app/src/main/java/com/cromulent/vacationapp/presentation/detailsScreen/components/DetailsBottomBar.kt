package com.cromulent.vacationapp.presentation.detailsScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.presentation.components.TraveloButton
import com.cromulent.vacationapp.ui.theme.VacationAppTheme

@Composable
fun DetailsBottomBar(
    modifier: Modifier = Modifier,
    priceRange: String?,
    openWebsite: () -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.white))
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .weight(1f)
        ) {
            Text(
                text = "Price Figure",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            var fontSize by remember { mutableStateOf(24.sp) }

            Text(
                modifier = Modifier.wrapContentWidth(),
                text = priceRange ?: "Unknown",
                color = if(priceRange == null) colorResource(R.color.subtitle) else colorResource(R.color.money_color),
                maxLines = 1,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow && fontSize > 16.sp) {
                        fontSize = fontSize * 0.9f
                    }
                }
            )
        }

        TraveloButton(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .weight(2f)
                .shadow(
                    elevation = 8.dp,
                    spotColor = colorResource(R.color.primary),
                    ambientColor = colorResource(R.color.primary)
                )
                .clip(RoundedCornerShape(16.dp)),
            text = "Visit Website",
            trailingIcon = Icons.AutoMirrored.Default.ArrowForward,
        ) {
            openWebsite()
        }


    }
}

@Preview
@Composable
private fun DetailsBottomBarPrev() {

    VacationAppTheme {
        DetailsBottomBar(
            priceRange = "$$$$"
        ) { }
    }
    
}