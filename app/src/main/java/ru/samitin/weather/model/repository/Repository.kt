package ru.samitin.weather.model.repository

import ru.samitin.weather.model.data.Weather

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}