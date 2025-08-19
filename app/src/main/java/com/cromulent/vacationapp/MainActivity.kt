package com.cromulent.vacationapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cromulent.vacationapp.presentation.detailsScreen.DetailsScreen
import com.cromulent.vacationapp.presentation.detailsScreen.DetailsViewmodel
import com.cromulent.vacationapp.presentation.navigation.MainNavGraph
import com.cromulent.vacationapp.ui.theme.VacationAppTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainViewmodel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VacationAppTheme {
                MainNavGraph(
                    modifier = Modifier,
                    startDestination = viewModel.startDestination
                )
            }
        }
    }
}