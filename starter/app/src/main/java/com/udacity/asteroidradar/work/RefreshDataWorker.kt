package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(context: Context, params: WorkerParameters):
        CoroutineWorker(context, params){



    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.refreshData()
            database.asteroidDao.deletePastAsteroids(repository.currentDateStr)
            Result.success()
        }catch(exception: HttpException){
            Result.retry()
        }
    }
}