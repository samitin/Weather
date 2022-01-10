package ru.samitin.weather.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.samitin.weather.model.Repository
import ru.samitin.weather.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(false)

    fun getWeatherFromRemoteSource() = getDataFromLocalSource(true)

    private fun getDataFromLocalSource(isRus:Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            if (isRus)
                liveDataToObserve.postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorageRus()))
            else
                liveDataToObserve.postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorageWorld()))
        }.start()
    }
}