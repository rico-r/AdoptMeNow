package com.kelompok5.adoptmenow.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.database.getValue
import com.kelompok5.adoptmenow.adoptform.AdoptionForm
import com.kelompok5.adoptmenow.network.FirebaseData
import com.kelompok5.adoptmenow.petinfo.PetInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AdoptHistoryViewModel : ViewModel() {
    val list = MutableLiveData(listOf<AdoptionForm>())
    val adoptedId = list.map { it.map { it.postId } }

    init {
        GlobalScope.launch(Dispatchers.IO) { onGetHistory() }
    }

    fun isAdopted(post: PetInfo): Boolean {
        return adoptedId.value?.contains(post.id) ?: false
    }

    fun add(form: AdoptionForm) {
        list.value = list.value!! + listOf(form)
        FirebaseData.formRef.push().setValue(form)
    }

    private suspend fun onGetHistory() {
        val formMap = FirebaseData.formRef.orderByChild("from")
            .equalTo(FirebaseData.uid).get().await().getValue<HashMap<String, AdoptionForm>>() ?: return
        val result = mutableListOf<AdoptionForm>()
        for(form in formMap.values) {
            form.post = FirebaseData.getPostData(form.postId) ?: return
            result.add(form)
        }
        result.sortBy { it.date }
        withContext(Dispatchers.Main) {
            this@AdoptHistoryViewModel.list.value = result
        }
    }
}