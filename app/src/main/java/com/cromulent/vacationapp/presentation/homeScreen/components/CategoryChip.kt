package com.cromulent.vacationapp.presentation.homeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.ui.theme.VacationAppTheme

@Composable
fun CategoryChip(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {

    FilterChip(
        modifier = modifier
            .height(42.dp),
        onClick = onClick,
        label = {
            Text(
                text = text,
                color = if (isSelected) colorResource(R.color.primary) else Color.LightGray,
                fontWeight = if(isSelected) FontWeight.SemiBold else FontWeight.W300
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = colorResource(R.color.background_secondary),
            containerColor = Color.Transparent
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = false,
            selected = false,
            borderColor = Color.Transparent,
            borderWidth = 0.dp,
            disabledBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(18.dp),
        selected = isSelected,
    )

//    Box(
//        modifier = modifier
//            .padding(
//                horizontal = 16.dp,
//                vertical = 12.dp
//            )
//            .background(
//                color = if (isSelected) colorResource(R.color.background_secondary) else Color.Transparent,
//                shape = RoundedCornerShape(16.dp)
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//
//        Text(
//            text = text,
//            color = if (isSelected) colorResource(R.color.primary) else Color.LightGray
//        )
//
//    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CatChipPrev() {
    VacationAppTheme {
        Box(
            Modifier.fillMaxSize().background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            Row {
                CategoryChip(
                    text = "Location",
                    isSelected = false
                ) {}
                CategoryChip(
                    text = "Hotels",
                    isSelected = true
                ) {}
                CategoryChip(
                    text = "Food",
                    isSelected = false
                ) {}
                CategoryChip(
                    text = "Adventure",
                    isSelected = false
                ) {}
            }
        }
    }
}