package com.kelompok5.adoptmenow.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.getValue
import com.kelompok5.adoptmenow.network.FirebaseData
import com.kelompok5.adoptmenow.petinfo.PetInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedViewModel : ViewModel() {

    val saved = MutableLiveData<List<PetInfo>>()
    val lastIndex = MutableLiveData(0)
    val savedMap = saved.map { list ->
        list.map { it.id }.zip(list).toMap()
    }

    init {
        FirebaseData.savedRef.get().addOnSuccessListener {
            GlobalScope.launch(Dispatchers.IO) { onGetSavedList(it) }
        }
    }

    private suspend fun onGetSavedList(snapshot: DataSnapshot) {
        // listId in format postId=>index
        val listId = snapshot.getValue<HashMap<String, Int>>() ?: return
        // Reverse the list to format index=>postId to maintain the order
        val reverse = HashMap<Int, String>()
        for(kv in listId.entries) {
            reverse[kv.value] = kv.key
        }
        val savedList = mutableListOf<PetInfo>()
        for(id in reverse.values) {
            if(isSaved(id)) continue
            val post = FirebaseData.getPostData(id) ?: continue
            savedList.add(post)
        }
        withContext(Dispatchers.Main) {
            saved.value = savedList
            lastIndex.value = reverse.keys.last() + 1
        }
    }

    fun isSaved(postId: String): Boolean {
        return savedMap.value?.contains(postId) ?: false
    }

    fun isSaved(post: PetInfo): Boolean {
        return isSaved(post.id)
    }

    fun addSaved(post: PetInfo) {
        val list = saved.value ?: listOf()
        saved.value = list + listOf(post)
        FirebaseData.savedRef.child(post.id).setValue(lastIndex.value)
        lastIndex.value = lastIndex.value?.inc()
    }

    fun removeSaved(post: PetInfo) {
        val list = saved.value ?: listOf()
        saved.value = list.filter { it.id != post.id }
        FirebaseData.savedRef.child(post.id).removeValue()
    }
}