package com.kelompok5.adoptmenow.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kelompok5.adoptmenow.petinfo.PetInfo

class SearchResultViewModel(
    initialQuery: String
) : ViewModel() {

    val query = MutableLiveData(initialQuery)
    val searchResult = MutableLiveData(listOf<PetInfo>())

    fun onSearch(originQuery: String) {
        val query = originQuery.lowercase()
        Firebase.database.getReference("posts").orderByChild("title")
            .startAt(query).endAt(query + "\uf8ff")
            .get().addOnSuccessListener {
                val items = it.getValue<HashMap<String, PetInfo>>()
                searchResult.value = items?.values?.toList() ?: listOf()
            }.addOnFailureListener {
                // TODO: Do something when failed to get the list
                it.printStackTrace()
            }
    }

}