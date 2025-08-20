package com.cromulent.vacationapp.presentation.detailsScreen.components
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.ui.theme.ColorPrimary

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 4,
    fontSize: TextUnit = 14.sp,
    lineHeight: TextUnit = 20.sp,
    color: Color = colorResource(R.color.subtitle_secondary)
) {
    var isExpanded by remember { mutableStateOf(false) }
    var hasOverflow by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        modifier = modifier
            .animateContentSize(),
        text = text,
        maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
        overflow = TextOverflow.Ellipsis,
        lineHeight = lineHeight,
        color = color,
        fontSize = fontSize,
        onTextLayout = { layoutResult ->
            textLayoutResult = layoutResult
            hasOverflow = layoutResult.hasVisualOverflow
        }
    )

    // Show expand/collapse button only if text overflows
    if (hasOverflow || isExpanded) {
        Spacer(Modifier.size(8.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clickable {
                    isExpanded = !isExpanded
                }
        ) {
            Text(
                fontWeight = FontWeight.W600,
                text = if (isExpanded) "Read Less" else "Read More",
                fontSize = 12.sp,
                color = colorResource(R.color.primary)
            )

            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                tint = ColorPrimary,
                contentDescription = null
            )
        }
    }
}