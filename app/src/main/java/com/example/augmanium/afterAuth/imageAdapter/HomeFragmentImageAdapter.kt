package com.example.augmanium.afterAuth.imageAdapter

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("setHomeFragmentImage")
fun setHomeFragmentImage(view: ImageView, resource: String) {
//    view.setImageResource(resource)
    Glide.with(view.context)
        .load(resource)
        .override(300, 200)
        .into(view);
}