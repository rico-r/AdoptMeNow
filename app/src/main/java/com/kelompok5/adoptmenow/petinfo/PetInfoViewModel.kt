package com.kelompok5.adoptmenow.petinfo

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map

class PetInfoViewModel(
    app: Application
) : AndroidViewModel(app) {
    val pet = MutableLiveData<PetInfo>()
    val status = pet.map { pet.value?.getStatus(app.resources) }
    val listVisibility = pet.map { if(pet.value?.images!!.size == 1) View.GONE else View.VISIBLE }
    val firstImage = pet.map { pet.value?.images!![0] }
    val images = listVisibility.map {
        if(it == View.VISIBLE) pet.value!!.images.drop(0)
        else listOf()
    }
}