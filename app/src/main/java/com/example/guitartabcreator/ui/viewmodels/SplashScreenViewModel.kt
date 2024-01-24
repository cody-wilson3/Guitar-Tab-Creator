package com.example.guitartabcreator.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.guitartabcreator.ui.repositories.UserRepository

class SplashScreenViewModel(application: Application): AndroidViewModel(application) {
    fun isUserLoggedIn() = UserRepository.isUserLoggedIn()
}