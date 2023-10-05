package com.foodbae.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var username: String? = null,
    var email: String? = null,
    val password: String?=null,
    var contact: String? = null,
    var postal_code: String? = null,
    var state: String? = null,
    var address: String? = null,
) : Parcelable
