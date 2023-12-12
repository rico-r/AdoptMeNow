package com.kelompok5.adoptmenow.adoptform

import androidx.recyclerview.widget.DiffUtil
import com.google.firebase.database.Exclude
import com.kelompok5.adoptmenow.petinfo.PetInfo
import java.io.Serializable

data class AdoptionForm(
    var from: String = "",
    var to: String = "",
    var response: String = "",
    var date: Long = 0L,
    var postId: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var profession: String = "",
    var address: String = "",
    var reason: String = "",
) : Serializable {
    @Exclude @JvmField
    var post: PetInfo? = null
}

class AdoptionFormDiffCallback : DiffUtil.ItemCallback<AdoptionForm>() {
    override fun areItemsTheSame(oldItem: AdoptionForm, newItem: AdoptionForm): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AdoptionForm, newItem: AdoptionForm): Boolean {
        return oldItem == newItem
    }
}
