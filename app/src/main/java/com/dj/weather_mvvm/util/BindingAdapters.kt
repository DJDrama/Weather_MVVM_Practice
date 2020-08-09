package com.dj.weather_mvvm.util

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dj.weather_mvvm.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        val imageUri = imageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imageUri)
            .apply(
                RequestOptions().placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
            )
            .into(imageView)
    }
}

@BindingAdapter("dateFormatted")
fun dateFormatted(tv: TextView, dt: Long) {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    tv.text = "Date: ${formatter.format(Date(dt * 1000L))}"
}