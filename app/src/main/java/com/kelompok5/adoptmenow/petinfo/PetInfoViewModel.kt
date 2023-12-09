package com.kelompok5.adoptmenow.petinfo

import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.kelompok5.adoptmenow.network.FirebaseData
import com.kelompok5.adoptmenow.saved.SavedViewModel

class PetInfoViewModel(
    private val savedViewModel: SavedViewModel
) : ViewModel() {
    val pet = MutableLiveData<PetInfo>()
    val listVisibility = pet.map { visibleIf(pet.value?.images!!.size > 1) }
    val firstImage = pet.map { pet.value?.images!![0] }
    val images = listVisibility.map {
        if(it == View.VISIBLE) pet.value!!.images.drop(0)
        else listOf()
    }
    private val _isSaved = MediatorLiveData<Boolean>().apply {
        val update: () -> Unit = {
            pet.value?.let {
                this.value = savedViewModel.isSaved(it)
            }
        }
        addSource(pet) { update() }
        addSource(savedViewModel.savedMap) { update() }
    }
    val saveButtonVisibility = _isSaved.map { visibleIf(!it) }
    val unsaveButtonVisibility = _isSaved.map { visibleIf(it) }
    val adoptButtonEnabled = pet.map { it.owner.isNotEmpty() && it.owner != FirebaseData.uid }

    private fun visibleIf(isVisible: Boolean): Int {
        return if(isVisible) View.VISIBLE else View.GONE
    }

    fun onSave() {
        pet.value?.let {
            savedViewModel.addSaved(it)
        }
    }

    fun onUnsave() {
        pet.value?.let {
            savedViewModel.removeSaved(it)
        }
    }
}
