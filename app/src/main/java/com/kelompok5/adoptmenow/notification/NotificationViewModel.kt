package com.kelompok5.adoptmenow.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.kelompok5.adoptmenow.network.FirebaseData

class NotificationViewModel : ViewModel(), ValueEventListener {
    val notification = MutableLiveData<List<NotificationItem>>()

    init {
        FirebaseData.notifyRef.child(FirebaseData.uid).addValueEventListener(this)
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        val newList = mutableListOf<NotificationItem>()
        for(item in snapshot.children) {
            val type = item.child("type").getValue<Int>()
            newList.add(when(type) {
                NOTIFICATION_ITEM_REQUEST -> item.getValue<NotificationItem.AdoptionRequest>()!!
                NOTIFICATION_ITEM_RESPONSE -> item.getValue<NotificationItem.AdoptionResponse>()!!
                else -> throw RuntimeException("Unknown notification with type $type")
            })
        }
        newList.sortByDescending { it.date }
        notification.value = newList
    }

    override fun onCancelled(error: DatabaseError) {}
}