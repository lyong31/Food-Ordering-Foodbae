package com.foodbae.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class OrderModel(
    var order_menu: String?=null,
    var order_quantity: Int?=0,
    var order_price: Int?=0,
    var order_date:String?=null
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "order_menu" to order_menu,
            "order_quantity" to order_quantity,
            "order_price" to order_price,
            "order_date" to order_date,
        )
    }
}
