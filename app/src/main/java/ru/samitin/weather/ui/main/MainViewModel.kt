package ru.samitin.weather.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(private val liveDataToObserve:MutableLiveData<Any> = MutableLiveData()) : ViewModel() {
    // TODO: Implement the ViewModel
    fun getData():LiveData<Any>{
        getDataFromLocalSource()
        return liveDataToObserve
    }
    private fun getDataFromLocalSource(){
        Thread{
            Thread.sleep(1000)
            liveDataToObserve.postValue(Any())
        }.start()
    }
}