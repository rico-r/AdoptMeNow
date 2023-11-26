package com.kelompok5.adoptmenow.account

import java.io.Serializable


data class UserInfo(
    var name: String = "",
    var photo: String = "",
    var email: String = "",
    var address: String = "",
    var phone: String = "",
) : Serializable
