package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.domain.Asteroid
import retrofit2.http.DELETE

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidDatabaseModel)

    @Query("SELECT * FROM asteroid_detail_table WHERE closeApproachDate >= :today ORDER BY closeApproachDate ASC")
    fun getSavedAsteroids(today: String): LiveData<List<AsteroidDatabaseModel>?> // not showing past records in the database

    @Query("DELETE FROM asteroid_detail_table WHERE closeApproachDate <= :today")
    fun deletePastAsteroids(today: String)

    @Query("SELECT * FROM asteroid_detail_table WHERE closeApproachDate = :today ORDER BY closeApproachDate ASC")
    fun getTodayAsteroids(today: String):LiveData<List<AsteroidDatabaseModel>?>

    @Query("SELECT * FROM asteroid_detail_table WHERE closeApproachDate >= :today ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(today:String):LiveData<List<AsteroidDatabaseModel>?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureDetails(picture:PictureDatabaseModel?)
    
    @Query("SELECT * FROM picture_table")
    fun getPictureDetails(): LiveData<PictureDatabaseModel?>
    
    @Query("DELETE FROM picture_table")
    fun deletePictureDetails()
}