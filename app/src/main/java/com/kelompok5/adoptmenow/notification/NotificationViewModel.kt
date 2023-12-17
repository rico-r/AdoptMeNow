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
            val newItem = when(type) {
                NOTIFICATION_ITEM_REQUEST -> item.getValue<NotificationItem.AdoptionRequest>()!!
                NOTIFICATION_ITEM_RESPONSE -> item.getValue<NotificationItem.AdoptionResponse>()!!
                else -> throw RuntimeException("Unknown notification with type $type")
            }
            newItem.id = item.key!!
            newList.add(newItem)
        }
        newList.sortByDescending { it.date }
        notification.value = newList
    }

    override fun onCancelled(error: DatabaseError) {}

    fun markRead(item: NotificationItem) {
        FirebaseData.notifyRef
            .child(FirebaseData.uid)
            .child(item.id)
            .child("readed")
            .setValue(true)

        when(item) {
            is NotificationItem.AdoptionRequest -> item.readed = true
            is NotificationItem.AdoptionResponse -> item.readed = true
            else -> throw RuntimeException()
        }
        notification.postValue(notification.value)
    }
}