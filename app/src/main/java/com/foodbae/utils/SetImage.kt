package com.foodbae.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.foodbae.R

object SetImage {

    fun ImageView.load(url: String) {
        Glide.with(this.context).setDefaultRequestOptions(
            RequestOptions()
                .override(150,150)
                .placeholder(R.drawable.screen)
                .error(R.drawable.ic_image)
        ).load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }
}