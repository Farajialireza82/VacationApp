package com.cromulent.vacationapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.ui.theme.VacationAppTheme
import kotlinx.coroutines.delay

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    hint: String,
    isSearchable: Boolean = true,
    onClick: () -> Unit = {},
    onSearch: (String) -> Unit = {},
) {

    var text by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(text) {
        if (text.isNotBlank()) {
            delay(500)
            onSearch(text)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = colorResource(R.color.background_secondary),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(
                enabled = isSearchable.not(),
                onClick = {
                    onClick()
                }
            )

    ) {
        TextField(
            value = text,
            enabled = isSearchable,
            onValueChange = { text = it },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(text)
                }
            ),
            placeholder = {
                Text(
                    text = hint,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colorResource(R.color.subtitle),
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    tint = colorResource(R.color.subtitle),
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }

}


@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    onValueChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = colorResource(R.color.background_secondary),
                shape = RoundedCornerShape(24.dp)
            )

    ) {
        TextField(
            value = text,
            onValueChange = onValueChanged,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchClicked() }
            ),
            placeholder = {
                Text(
                    text = hint,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colorResource(R.color.subtitle),
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    tint = colorResource(R.color.subtitle),
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }

}

@Preview
@Composable
private fun SearchFieldPrev() {
    VacationAppTheme {
        SearchField(
            hint = "Find things to do"
        ) {}
    }
}