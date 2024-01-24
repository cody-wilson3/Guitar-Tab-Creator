package com.example.guitartabcreator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.guitartabcreator.ui.navigation.Routes
import com.example.guitartabcreator.ui.viewmodels.SplashScreenViewModel
import kotlinx.coroutines.*

@Composable
fun SplashScreen(navHostController: NavHostController) {

    val viewModel: SplashScreenViewModel = viewModel()

    LaunchedEffect(true) {
        // wait for 3 seconds or until the login check is
        // done before navigating
        delay(1)
//        loginStatusCheck.await()
        navHostController.navigate(
            if (viewModel.isUserLoggedIn()) {
                Routes.appNavigation.route
            } else {
                Routes.launchNavigation.route
            }
        ){
//            if (UserRepository.getCurrentUserId() == null) Routes.launchNavigation.route else Routes.appNavigation.route) {
            // makes it so that we can't get back to the
            // splash screen by pushing the back button
            popUpTo(navHostController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Guitar Tabs",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "USU-CS3200",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
    }
}