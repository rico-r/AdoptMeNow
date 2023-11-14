package com.kelompok5.adoptmenow.petinfo

import android.content.res.Resources
import com.kelompok5.adoptmenow.R
import java.io.Serializable

data class PetInfo(
    var dataUrl: String,
    var title: String,
    var available: Boolean,
    var description: String,
    var phone: String,
    var address: String,
    var images: List<String>
) : Serializable {
    fun getStatus(resources: Resources): CharSequence {
        return resources.getText(if(available) R.string.status_available else R.string.status_not_available)
    }
}
