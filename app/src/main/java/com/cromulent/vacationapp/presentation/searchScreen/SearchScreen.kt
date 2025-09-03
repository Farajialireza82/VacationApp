package com.cromulent.vacationapp.presentation.searchScreen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.model.Category
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.presentation.components.SearchField
import com.cromulent.vacationapp.presentation.homeScreen.components.CategoryChip
import com.cromulent.vacationapp.presentation.util.TestTags
import com.cromulent.vacationapp.presentation.util.TestTags.SEARCH_SCREEN_EMPTY_STATE
import com.cromulent.vacationapp.presentation.util.TestTags.SEARCH_SCREEN_START_STATE
import com.cromulent.vacationapp.ui.theme.VacationAppTheme
import com.cromulent.vacationapp.util.Constants.CATEGORIES
import com.cromulent.vacationapp.util.Constants.SEARCH_CATEGORIES
import com.cromulent.vacationapp.util.Samples
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewmodel: SearchViewmodel = hiltViewModel<SearchViewmodel>(),
    openDetailsScreen: (locationId: String) -> Unit
) {

    val state = viewmodel.state.collectAsState()

    val backDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    var selectedCategory by rememberSaveable { mutableStateOf(SEARCH_CATEGORIES[0]) }

    var searchQuery by rememberSaveable { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    var isFirstRecomposition by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(true) {

        //wrapped in try catch to avoid crashes during test runs
        try {
            if (isFirstRecomposition) {
                focusRequester.requestFocus()
                isFirstRecomposition = false
            }
        }catch (_: Exception){}

    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(vertical = 12.dp),
                title = {

                    Text(
                        text = "Search",
                        fontWeight = FontWeight.SemiBold
                    )

                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .testTag(TestTags.BACK_BUTTON)
                            .padding(horizontal = 12.dp)
                            .size(50.dp)
                            .background(
                                color = colorResource(R.color.background_secondary),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape((8.dp)))
                            .clickable {
                                backDispatcher?.onBackPressed()
                            }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(12.dp)
                                .fillMaxSize()
                                .align(Alignment.Center),
                            painter = painterResource(R.drawable.ic_left),
                            tint = colorResource(R.color.subtitle),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {

        Column(
            modifier = Modifier
                .padding(it)
        ) {
            SearchField(
                text = searchQuery,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                hint = "Where do you want to go?",
                focusRequester = focusRequester,
                onValueChanged = { searchQuery = it },
                search = {
                    viewmodel.search(selectedCategory.key, searchQuery)
                }
            )

            Spacer(Modifier.size(22.dp))

            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(SEARCH_CATEGORIES) { category ->
                    CategoryChip(
                        modifier = Modifier
                            .testTag(category.key),
                        text = category.title,
                        isSelected = selectedCategory == category
                    ) {
                        selectedCategory = category
                        if (searchQuery.isNotBlank()) {
                            viewmodel.search(selectedCategory.key, searchQuery)
                        }
                    }
                }
            }

            Spacer(Modifier.size(22.dp))

            HorizontalDivider(
                color = colorResource(R.color.background_secondary)
            )

            Spacer(Modifier.size(22.dp))


            if (state.value.isLoading) {
                LoadingState(
                    modifier = Modifier
                        .testTag(TestTags.SEARCH_SCREEN_LOADING_STATE)
                )
                return@Scaffold
            }

            if (state.value.data == null) {

                EmptyState(
                    modifier = Modifier
                        .testTag(SEARCH_SCREEN_START_STATE),
                    title = "Start exploring",
                    subtitle = "Find hotels, attractions, and restaurants worldwide"
                )
                return@Scaffold


            }

            if (state.value.data?.isEmpty() == true) {
                EmptyState(
                    modifier = Modifier
                        .testTag(SEARCH_SCREEN_EMPTY_STATE)
                )
                return@Scaffold

            }

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                items(state.value.data!!) {

                    SearchItem(
                        location = it
                    ) {
                        openDetailsScreen(it.locationId)
                    }

                    Spacer(Modifier.size(12.dp))

                }
            }
        }

    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
    title: String = "No Results Found",
    subtitle: String? = "No Results Found"
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(vertical = 48.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(90.dp)
                .background(
                    color = colorResource(R.color.background_secondary),
                    shape = CircleShape
                )
        ) {
            Icon(
                modifier = Modifier
                    .size(42.dp)
                    .fillMaxSize()
                    .align(Alignment.Center),
                painter = painterResource(R.drawable.ic_search),
                tint = colorResource(R.color.subtitle),
                contentDescription = null
            )
        }

        Spacer(Modifier.size(24.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = title,
            fontWeight = FontWeight.W600,
            fontSize = 24.sp
        )

        subtitle?.let {

            Spacer(Modifier.size(8.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = subtitle,
                color = colorResource(R.color.hint_color),
                fontSize = 16.sp
            )
        }

    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(vertical = 48.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Spacer(Modifier.size(24.dp))

        Text(
            text = "Searching...",
            color = colorResource(R.color.hint_color),
            style = MaterialTheme.typography.bodyLarge
        )

    }
}

@Composable
private fun SearchItem(
    modifier: Modifier = Modifier,
    location: Location,
    onClick: () -> Unit
) {

    ElevatedCard(
        modifier = modifier
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.White
        ),
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(120.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                modifier = Modifier,
                text = location.name,
                fontWeight = FontWeight.W600,
                fontSize = 18.sp
            )

            Spacer(Modifier.size(8.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier
                        .size(12.dp),
                    imageVector = Icons.Outlined.LocationOn,
                    tint = colorResource(R.color.hint_color),
                    contentDescription = "Location Icon"
                )

                Spacer(Modifier.size(4.dp))

                Text(
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    text = location.addressObject?.getAddressTitle() ?: "ADDRESS TITLE",
                    color = colorResource(R.color.hint_color),
                    fontSize = 12.sp
                )
            }

        }

    }


}

@Preview
@Composable
private fun Preview() {

    VacationAppTheme {

        SearchScreen{}
    }

}