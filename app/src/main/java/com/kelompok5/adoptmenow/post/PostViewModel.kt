package com.kelompok5.adoptmenow.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kelompok5.adoptmenow.network.FirebaseData
import com.kelompok5.adoptmenow.petinfo.PetInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PostViewModel : ViewModel() {
    val list = MutableLiveData(listOf<PetInfo>())

    init {
        FirebaseData.postRef.orderByChild("owner")
            .equalTo(FirebaseData.uid).get()
            .addOnSuccessListener { snapshot ->
                val items = snapshot.getValue<HashMap<String, PetInfo>>() ?: return@addOnSuccessListener
                items.forEach { it.value.id = it.key }
                val list = items.values.toList()
                this.list.value = list
            }
    }

    suspend fun deletePost(post: PetInfo, onDeleteSuccess: () -> Unit) {
        // Delete the images
        post.images.forEach {
            Firebase.storage.getReference(it).delete().await()
        }
        FirebaseData.postRef.child(post.id).removeValue().await()
        withContext(Dispatchers.Main) {
            list.value = list.value?.filterNot { it.id == post.id }
            onDeleteSuccess()
        }
    }

    suspend fun createPost(post: PetInfo, onSuccess: () -> Unit) {
        FirebaseData.createPost(post).await()
        withContext(Dispatchers.Main) {
            val tmpList = list.value ?: listOf()
            list.value = tmpList + listOf(post)
            onSuccess()
        }
    }

}