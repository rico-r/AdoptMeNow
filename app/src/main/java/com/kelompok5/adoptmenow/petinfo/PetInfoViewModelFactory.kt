package com.kelompok5.adoptmenow.petinfo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PetInfoViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetInfoViewModel::class.java)) {
            return PetInfoViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}