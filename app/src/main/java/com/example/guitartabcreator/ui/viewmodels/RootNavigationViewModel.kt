package com.example.guitartabcreator.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.guitartabcreator.ui.repositories.UserRepository

class RootNavigationViewModel(application: Application): AndroidViewModel(application) {
    fun signUserOut() {
        UserRepository.signOutUser()
    }
}