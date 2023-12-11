package com.kelompok5.adoptmenow.notification

import androidx.recyclerview.widget.DiffUtil
import java.util.Date

const val NOTIFICATION_ITEM_REQUEST = 0
const val NOTIFICATION_ITEM_RESPONSE = 1
const val NOTIFICATION_ITEM_DIVIDER = 2

sealed class NotificationItem(
    val type: Int = -1,
    var date: Long
) {
    class AdoptionRequest(
        var name: String = "",
        var title: String = "",
        var src: String = "",
        var postId: String = "",
        date: Long = 0L,
        var readed: Boolean = false,
    ): NotificationItem(NOTIFICATION_ITEM_REQUEST, date)

    class AdoptionResponse(
        var name: String = "",
        var src: String = "",
        var postId: String = "",
        date: Long = 0L,
        var readed: Boolean = false,
    ): NotificationItem(NOTIFICATION_ITEM_RESPONSE, date)

    class Divider(date: Long = 0L): NotificationItem(NOTIFICATION_ITEM_DIVIDER, date) {
        companion object {
            fun getString(time: Long): String {
                var diff = (Date().time - time) / 1000L
                if (diff < 60) return "$diff detik lalu"
                diff /= 60
                if (diff < 60) return "$diff menit lalu"
                diff /= 60
                if (diff < 24) return "$diff jam lalu"
                diff /= 24
                if (diff < 30) return "$diff hari lalu"
                diff /= 30
                if (diff < 12) return "$diff bulan lalu"
                diff /= 12
                return "$diff tahun lalu"
            }
        }
    }
}

class NotificationItemDiffCallback : DiffUtil.ItemCallback<NotificationItem>() {
    override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
        return oldItem.type == newItem.type
    }
    override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
        return oldItem == newItem
    }
}