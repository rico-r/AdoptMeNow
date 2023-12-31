package com.kelompok5.adoptmenow.petinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kelompok5.adoptmenow.history.AdoptHistoryViewModel
import com.kelompok5.adoptmenow.saved.SavedViewModel

class PetInfoViewModelFactory(
    private val savedViewModel: SavedViewModel,
    private val adoptHistoryViewModel: AdoptHistoryViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetInfoViewModel::class.java)) {
            return PetInfoViewModel(savedViewModel, adoptHistoryViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}