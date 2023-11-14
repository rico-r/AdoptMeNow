package com.kelompok5.adoptmenow

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@BindingAdapter("imageSrc")
fun bindImage(imgView: ImageView, url: String?) {
    url?.let {
        val ref = Firebase.storage.reference.child(url)
        Glide.with(imgView.context)
            .load(ref)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.pets_long)
                    .error(R.drawable.pets_long))
            .into(imgView)
    }
}
