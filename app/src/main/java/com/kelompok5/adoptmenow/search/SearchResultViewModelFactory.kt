package com.kelompok5.adoptmenow.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchResultViewModelFactory(
    private val initialQuery: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchResultViewModel::class.java)) {
            return SearchResultViewModel(initialQuery) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}