package com.kelompok5.adoptmenow.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AccountViewModel : ViewModel() {
    val user = MutableLiveData<UserInfo>()
    val canEdit = user.map {
        it != null
    }
    val photo = user.map {
        if(it.photo == "") "/default-profile.jpg"
        else it.photo
    }

    init {
        Firebase.database.getReference("users")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user.value = snapshot.getValue(UserInfo::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}