package com.example.guitartabcreator.ui

import androidx.compose.runtime.Composable
import com.example.guitartabcreator.ui.navigation.RootNavigation
import com.example.guitartabcreator.ui.theme.GuitarTabCreatorTheme

@Composable
fun App() {
    GuitarTabCreatorTheme {
        RootNavigation()
    }
}