package com.foodbae.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseMenu(
    @field:SerializedName("data")
    var data: ArrayList<FeelingModel>
)

@Parcelize
data class FeelingModel(
    @field:SerializedName("image_feeling")
    var image:String,

    @field:SerializedName("type_feeling")
    var feeling : String?=null,

    @field:SerializedName("id")
    var id: Int?=0,

    @field:SerializedName("results")
    var results: ArrayList<FoodbaeModel>?=null
) : Parcelable

@Parcelize
data class FoodbaeModel(
    @field:SerializedName("type_menu_id")
    var id: Int?=0,

    @field:SerializedName("feeling")
    var feeling:String?=null,

    @field:SerializedName("type_menu")
    var type_menu: String?=null,

    @field:SerializedName("menu_list")
    var menu_list: ArrayList<MenuModel>
) : Parcelable