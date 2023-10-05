package com.foodbae.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class CartModel(
    var key: String? = null,
    var menu_id: String? = null,
    var menu_name: String? = null,
    var menu_price: Int = 0,
    var image: String? = null,
    var size: String? = null,
    var quantity: Int = 0,
    var total_price: Int = 0,
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "key" to key,
            "menu_id" to menu_id,
            "menu_name" to menu_name,
            "menu_price" to menu_price,
            "image" to image,
            "size" to size,
            "quantity" to quantity,
            "total_price" to total_price
        )
    }
}
