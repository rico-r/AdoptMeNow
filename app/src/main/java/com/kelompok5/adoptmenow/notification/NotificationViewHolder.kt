package com.kelompok5.adoptmenow.notification

import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.ListItemNotificationBinding
import com.kelompok5.adoptmenow.databinding.ListItemNotificationDividerBinding

class NotificationViewHolder(val binding: ListItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindRequest(item: NotificationItem.AdoptionRequest) {
        val res = binding.root.context.resources
        binding.title.text = item.name
        binding.description.text = res.getString(R.string.notification_adopt_request, item.name, item.title)
    }

    fun bindResponse(item: NotificationItem.AdoptionResponse) {
        val res = binding.root.context.resources
        binding.title.text = item.name
        binding.description.text = res.getString(R.string.notification_adopt_response, item.name)
    }
}

class NotificationDividerViewHolder(val binding: ListItemNotificationDividerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: NotificationItem.Divider) {
        binding.text.text = NotificationItem.Divider.getString(item.date)
    }
}