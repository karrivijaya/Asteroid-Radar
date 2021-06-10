package com.udacity.asteroidradar

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidApiStatus
import timber.log.Timber

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = "Photo of hazardous asteroid"
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = "Photo of not hazardous asteroid"
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("loadingImage")
fun bindLoadingImage(imageView: ImageView, status: AsteroidApiStatus){
    when(status){
        AsteroidApiStatus.LOADING -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.loading_animation)
        }

        AsteroidApiStatus.DONE -> {
            imageView.visibility = View.GONE
        }

        AsteroidApiStatus.ERROR -> {
            imageView.visibility = View.GONE

        }

    }
}

@BindingAdapter("loadPictureOfTheDayUrl","imageType")
fun bindMainImage(imageView: ImageView, loadPictureOfTheDayUrl: String?, imageType: String?) {
    if (imageType.equals("image")) {
        Picasso.get()
                .load(loadPictureOfTheDayUrl).fit().centerCrop()
                .placeholder(R.drawable.placeholder_picture_of_day)
                .error(R.drawable.ic_connection_error)
                .into(imageView)
    } else {
        imageView.setImageResource(R.drawable.placeholder_picture_of_day)
    }
}

