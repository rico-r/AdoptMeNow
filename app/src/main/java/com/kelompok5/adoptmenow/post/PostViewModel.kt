package com.kelompok5.adoptmenow.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.getValue
import com.kelompok5.adoptmenow.network.FirebaseData
import com.kelompok5.adoptmenow.petinfo.PetInfo

class PostViewModel : ViewModel() {
    val list = MutableLiveData(listOf<PetInfo>())

    init {
        FirebaseData.postRef.orderByChild("owner")
            .equalTo(FirebaseData.uid).get()
            .addOnSuccessListener { snapshot ->
                val items = snapshot.getValue<HashMap<String, PetInfo>>() ?: return@addOnSuccessListener
                items.forEach { it.value.id = it.key }
                list.value = items.values.toList()
            }
    }
}