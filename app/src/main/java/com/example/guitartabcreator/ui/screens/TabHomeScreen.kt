package com.example.guitartabcreator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guitartabcreator.ui.viewmodels.TabsViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.viewinterop.AndroidView
import com.example.guitartabcreator.ui.components.GuitarTabItem
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun TabHomeScreen(navHostController: NavHostController, id: String?) {
    val viewModel: TabsViewModel = viewModel()
    val state = remember {viewModel.uiState}

    if (id != "" ) {
        state._tabList.removeIf { tab -> tab.id == id }
    }


    LaunchedEffect(true) {
        viewModel.getTabs()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Tabs Home Page!",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
    }

    LazyColumn {
        items(state.tabList, key = {it.id!!}) {theTab ->
            GuitarTabItem(
                tab = theTab,
                onEditPressed = { navHostController.navigate("edittab?id=${theTab.id}") },
                onDeletePressed = { navHostController.navigate("deleteconfirmation?id=${theTab.id}")                }
            )
        }
    }

    // ad
    // ca-app-pub-3940256099942544/6300978111
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Bottom),

        factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}