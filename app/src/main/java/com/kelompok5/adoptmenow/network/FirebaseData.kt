package com.kelompok5.adoptmenow.network

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.kelompok5.adoptmenow.account.UserInfo
import com.kelompok5.adoptmenow.petinfo.PetInfo
import kotlinx.coroutines.tasks.await
import kotlin.random.Random
import kotlin.random.nextUInt

class FirebaseData {

    companion object {

        val uid: String
            get() = Firebase.auth.currentUser!!.uid

        val userRef: DatabaseReference
            get() = Firebase.database.getReference("users").child(uid)

        val savedRef: DatabaseReference
            get() = Firebase.database.getReference("saved").child(uid)

        val postRef: DatabaseReference
            get() = Firebase.database.getReference("posts")

        val formRef: DatabaseReference
            get() = Firebase.database.getReference("forms")

        val notifyRef: DatabaseReference
            get() = Firebase.database.getReference("notify")

        suspend fun uploadFiles(files: List<Uri>): List<String> {
            val fileRef: StorageReference = Firebase.storage.getReference(
                "/users/${uid}")
            val result = mutableListOf<String>()
            files.forEach { file ->
                val fileName = Random.nextUInt().toString(16)
                val uploadResult = fileRef.child(fileName).putFile(file).await()
                result.add(uploadResult.storage.path)
            }
            return result
        }

        fun getCurrentUser(callback: (UserInfo) -> Unit) {
            Firebase.database.getReference("users")
                .child(uid)
                .get().addOnSuccessListener {
                    callback(it.getValue(UserInfo::class.java)!!)
                }
        }

        fun createPost(post: PetInfo): Task<Void> {
            val ref = postRef.push()
            post.id = ref.key ?: ""
            return ref.setValue(post)
        }

        fun getPostRef(id: String): DatabaseReference {
            return postRef.child(id)
        }

        fun getPostData(id: String, onSuccess: (PetInfo?) -> Unit) {
            getPostRef(id).get().addOnSuccessListener {
                val result = it.getValue<PetInfo>()
                result?.id = id
                onSuccess(result)
            }
        }

        suspend fun getPostData(id: String): PetInfo? {
            val snapshot = getPostRef(id).get().await()
            val result = snapshot.getValue<PetInfo>()
            result?.id = id
            return result
        }

    }
}
