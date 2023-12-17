package com.kelompok5.adoptmenow.notification

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.databinding.ListItemNotificationBinding
import com.kelompok5.adoptmenow.databinding.ListItemNotificationDividerBinding

class NotificationViewHolder(
    val binding: ListItemNotificationBinding,
    val onRequestClick: (NotificationItem.AdoptionRequest) -> Unit,
    val onResponseClick: (NotificationItem.AdoptionResponse) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bindRequest(item: NotificationItem.AdoptionRequest) {
        val res = binding.root.context.resources
        binding.title.text = item.name
        binding.description.text = res.getString(R.string.notification_adopt_request, item.name, item.title)
        binding.readIndicator.visibility = if(item.readed) View.GONE else View.VISIBLE
        binding.root.setOnClickListener {
            onRequestClick(item)
        }
    }

    fun bindResponse(item: NotificationItem.AdoptionResponse) {
        val res = binding.root.context.resources
        binding.title.text = item.name
        binding.description.text = res.getString(R.string.notification_adopt_response, item.name)
        binding.readIndicator.visibility = if(item.readed) View.GONE else View.VISIBLE
        binding.root.setOnClickListener {
            onResponseClick(item)
        }
    }
}

class NotificationDividerViewHolder(val binding: ListItemNotificationDividerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: NotificationItem.Divider) {
        binding.text.text = NotificationItem.Divider.getString(item.date)
    }
}