package com.kelompok5.adoptmenow.petinfo

import androidx.recyclerview.widget.DiffUtil
import com.google.firebase.database.Exclude
import com.kelompok5.adoptmenow.R
import java.io.Serializable

data class PetInfo(
    @Exclude
    var id: String = "",
    var owner: String = "",
    var title: String = "",
    var available: Boolean = false,
    var description: String = "",
    var phone: String = "",
    var address: String = "",
    var images: List<String> = listOf()
) : Serializable {
    fun getStatus(): Int {
        return if(available) R.string.status_available else R.string.status_not_available
    }
}

class PetInfoDiffCallback : DiffUtil.ItemCallback<PetInfo>() {
    override fun areItemsTheSame(oldItem: PetInfo, newItem: PetInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PetInfo, newItem: PetInfo): Boolean {
        return oldItem == newItem
    }
}