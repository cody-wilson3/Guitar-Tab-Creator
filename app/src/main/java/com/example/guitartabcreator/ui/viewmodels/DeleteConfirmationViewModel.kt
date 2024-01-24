package com.example.guitartabcreator.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guitartabcreator.ui.repositories.TabRepository
import kotlinx.coroutines.launch

class DeleteConfirmationViewModel : ViewModel() {
    // Assuming you are using ViewModel to manage data
    // Inject your TabRepository instance or call deleteTab directly here
    fun deleteTab(id: String) {
        viewModelScope.launch {
            TabRepository.deleteTab(id)
        }
    }
}