package com.kelompok5.adoptmenow.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.databinding.ListItemNotificationBinding
import com.kelompok5.adoptmenow.databinding.ListItemNotificationDividerBinding

class NotificationAdapter(
    val onRequestClick: (NotificationItem.AdoptionRequest) -> Unit,
    val onResponseClick: (NotificationItem.AdoptionResponse) -> Unit
) : ListAdapter<NotificationItem, RecyclerView.ViewHolder>(NotificationItemDiffCallback()) {

    fun updateList(list: List<NotificationItem>) {
        if(list.isEmpty()) {
            submitList(list)
            return
        }
        val labeledList = mutableListOf<NotificationItem>()
        var lastLabel = ""
        var i = 0
        do {
            val item = list[i]
            val label = NotificationItem.Divider.getString(item.date)
            if(label != lastLabel) {
                labeledList.add(NotificationItem.Divider(item.date))
                lastLabel = label
            }
            labeledList.add(item)
            i++
        } while(i < list.size)
        submitList(labeledList)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            NOTIFICATION_ITEM_DIVIDER ->
                NotificationDividerViewHolder(
                    ListItemNotificationDividerBinding.inflate(layoutInflater, parent, false))
            else ->
                NotificationViewHolder(
                    ListItemNotificationBinding.inflate(layoutInflater, parent, false),
                    onRequestClick, onResponseClick)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when(item.type) {
            NOTIFICATION_ITEM_DIVIDER ->
                (holder as NotificationDividerViewHolder).bind(item as NotificationItem.Divider)
            NOTIFICATION_ITEM_RESPONSE ->
                (holder as NotificationViewHolder).bindResponse(item as NotificationItem.AdoptionResponse)
            NOTIFICATION_ITEM_REQUEST ->
                (holder as NotificationViewHolder).bindRequest(item as NotificationItem.AdoptionRequest)
        }
    }
}