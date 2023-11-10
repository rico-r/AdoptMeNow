package com.kelompok5.adoptmenow.petinfo

import java.io.Serializable

data class PetInfo(
    var dataUrl: String,
    var title: String,
    var status: Status,
    var description: String,
    var phone: String,
    var address: String,
    var images: List<String>
) : Serializable {
    enum class Status {
        Available, Closed, Rejected, Pending
    }
}
