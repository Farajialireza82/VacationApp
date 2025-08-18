package com.cromulent.vacationapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.viewpager.widget.ViewPager
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.presentation.components.CompactLocationCard
import com.cromulent.vacationapp.presentation.components.CompactLocationCardList
import com.cromulent.vacationapp.presentation.components.FullLocationCardList
import com.cromulent.vacationapp.presentation.components.SearchField
import com.cromulent.vacationapp.presentation.home.components.CategoryChip
import com.cromulent.vacationapp.presentation.home.components.HomeTopBar
import com.cromulent.vacationapp.util.Category
import com.cromulent.vacationapp.util.Constants.CATEGORIES
import kotlin.math.sin

@Composable
fun HomeScreen(
    viewmodel: HomeViewmodel,
    modifier: Modifier = Modifier
) {

    val state = viewmodel.state.collectAsState()
    var selectedCategory by remember { mutableStateOf(CATEGORIES[0]) }

    LaunchedEffect(selectedCategory) {
        viewmodel.getNearbyLocations("42.3455,-71.10767", selectedCategory.key)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            HomeTopBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .padding(horizontal = 24.dp)
            )
        }
    ) { paddingValues ->

        Column(
            Modifier.padding(paddingValues)
        ) {
            SearchField(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            )

            Spacer(Modifier.size(18.dp))

            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(CATEGORIES) { category ->
                    CategoryChip(
                        text = category.title,
                        isSelected = selectedCategory == category
                    ) {
                        selectedCategory = category
                    }
                }
            }

            Spacer(Modifier.size(32.dp))

            FullLocationCardList(
                modifier = Modifier
                    .padding(start = 24.dp),
                listTitle = "Popular",
                locations = state.value.locations,
                isLoading = state.value.isLoading,
                onLocationClicked = {},
                onSeeAllClicked = {},
            )

            Spacer(Modifier.size(32.dp))

            CompactLocationCardList(
                modifier = Modifier
                    .padding(start = 24.dp),
                listTitle = "Recommended",
                locations = state.value.locations,
                isLoading = state.value.isLoading,
                onLocationClicked = {},
            )

        }
    }

}