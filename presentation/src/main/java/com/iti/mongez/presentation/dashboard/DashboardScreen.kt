package com.iti.mongez.presentation.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DashboardScreen(
    // viewModel: DashboardViewModel = hiltViewModel()
) {
    // You will use your AppAiSuggestionCard and AppDeadlineCard here!
    Column(modifier = Modifier.fillMaxSize()) {
        Text("This is the actual Dashboard Screen")
    }
}