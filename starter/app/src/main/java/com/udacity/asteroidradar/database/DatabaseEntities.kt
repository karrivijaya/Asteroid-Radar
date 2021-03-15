package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay

@Entity(tableName = "asteroid_detail_table")
data class AsteroidDatabaseModel(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

fun List<AsteroidDatabaseModel>.asDomainModel(): List<Asteroid>{
    return map{
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun List<Asteroid>.asDatabaseModel(): Array<AsteroidDatabaseModel> {

    return map {
        AsteroidDatabaseModel(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

@Entity(tableName = "picture_table")
data class PictureDatabaseModel(
    @PrimaryKey
    val url: String,
    val mediaType: String,
    val title: String,
    )



fun PictureDatabaseModel.asDomainModel(): PictureOfDay{
    return PictureOfDay(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title,
    )
}

fun PictureOfDay.asDatabaseModel():PictureDatabaseModel{
    return PictureDatabaseModel(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title,
    )
}