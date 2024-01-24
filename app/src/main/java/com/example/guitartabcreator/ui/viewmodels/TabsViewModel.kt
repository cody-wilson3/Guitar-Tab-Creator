package com.example.guitartabcreator.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.example.guitartabcreator.ui.models.Tab
import com.example.guitartabcreator.ui.repositories.TabRepository

class TabsScreenState {
    val _tabList = mutableStateListOf<Tab>()
    val tabList: List<Tab> get() = _tabList
}

class TabsViewModel(application: Application): AndroidViewModel(application) {
    val uiState = TabsScreenState()


    // just gets the avatars from the AvatarRepository and adds
    // them to this uiState
    suspend fun getTabs(): List<Tab> {
        val tabs = TabRepository.getTabs()
        uiState._tabList.clear()
        uiState._tabList.addAll(tabs)
        return tabs
    }
}