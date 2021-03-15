package com.udacity.asteroidradar.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class PictureOfDay(
        @Json(name = "media_type")
        val mediaType: String,
        val title: String,
        val url: String)