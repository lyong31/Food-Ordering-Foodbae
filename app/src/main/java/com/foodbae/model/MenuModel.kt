package com.foodbae.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuModel(
    @field:SerializedName("menu_id")
    var id: String?=null,

    @field:SerializedName("menu_name")
    var name: String?=null,

    @field:SerializedName("menu_description")
    var description: String?=null,

    @field:SerializedName("menu_price")
    var price : Int=0,

    @field:SerializedName("menu_image")
    var image: String?,

    @field:SerializedName("menu_size")
    var size: String?=null
) : Parcelable
