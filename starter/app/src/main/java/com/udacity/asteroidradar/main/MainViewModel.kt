package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

enum class AsteroidApiStatus{
    LOADING,
    DONE,
    ERROR
}

enum class AsteroidApiFilter(val value:String){
    SHOW_WEEK("week"),
    SHOW_TODAY("today"),
    SHOW_SAVED("saved")
}

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AsteroidDatabase.getInstance(app)
    private val asteroidRepository = AsteroidRepository(database)

    private val _apiStatus = MutableLiveData<AsteroidApiStatus>()
    val apiStatus: LiveData<AsteroidApiStatus>
    get() = _apiStatus

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()

    var dataUpdated: Boolean = false

    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    private val _updateRecycleViewList = MutableLiveData<List<Asteroid>>()
    val updateRecycleViewList: LiveData<List<Asteroid>>
        get() = _updateRecycleViewList

    // to observe the updated list in view model and assigning it to recycleview list which in turn notifies the fragment of the change in list
    private val asteroidListObserver = Observer<List<Asteroid>?> {
        _updateRecycleViewList.value = it
    }

    private var asteroidListLiveData: LiveData<List<Asteroid>?> = MutableLiveData()


    private val _updatePicture = MutableLiveData<PictureOfDay>()
    val updatePicture:LiveData<PictureOfDay>
    get()= _updatePicture

    private val updatePictureObserver = Observer<PictureOfDay?> {
        _updatePicture.value = it
    }

    private var updatePictureLiveData: LiveData<PictureOfDay?> = MutableLiveData()


    init{
        viewModelScope.launch {
            setUpLiveData()  // initially fetching data from database if any, to reduce loading time

            refreshData()   // fetching from end point
            refreshPicture() // fetching from end point
        }

    }

    private fun setUpLiveData(){
        asteroidListLiveData = asteroidRepository.weeklyAsteroids  // retrieving from database
        asteroidListLiveData.observeForever(asteroidListObserver)

        updatePictureLiveData = asteroidRepository.pictureFromDatabase  // retrieving from database
        updatePictureLiveData.observeForever(updatePictureObserver)

    }

    fun refreshData() {
        viewModelScope.launch {
            _apiStatus.value = AsteroidApiStatus.LOADING
            // refreshing data
            try {
                asteroidRepository.refreshData()
                _apiStatus.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                Timber.i("viewmodel error: ${e.printStackTrace()}")
                _apiStatus.value = AsteroidApiStatus.ERROR
            }
        }
    }


    private fun refreshPicture(){
        viewModelScope.launch{
            try {
                // fetching picture
                asteroidRepository.fetchPictureOfTheDay()
            }catch(e: Exception){
            }
        }
    }



    fun updateFilter(filter: AsteroidApiFilter){
        asteroidListLiveData.removeObserver(asteroidListObserver)
        when(filter){
            AsteroidApiFilter.SHOW_WEEK -> {
                asteroidListLiveData = asteroidRepository.weeklyAsteroids
            }
            AsteroidApiFilter.SHOW_TODAY -> {
                asteroidListLiveData = asteroidRepository.todayAsteroids
            }
            AsteroidApiFilter.SHOW_SAVED -> {
                asteroidListLiveData = asteroidRepository.savedAsteroids
            }
        }
        asteroidListLiveData.observeForever(asteroidListObserver)
    }


    fun displayAsteroidDetails(asteroid: Asteroid){
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete(){
        _navigateToSelectedAsteroid.value = null
    }


    override fun onCleared() {
        super.onCleared()
        asteroidListLiveData.removeObserver(asteroidListObserver)
        updatePictureLiveData.removeObserver(updatePictureObserver)
    }

    fun setUpdateData() {
        dataUpdated = true
    }


    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}