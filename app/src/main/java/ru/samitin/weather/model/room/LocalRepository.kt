package ru.samitin.weather.model.room

import ru.samitin.weather.model.data.Weather

interface LocalRepository {

    fun getAllHistory() : List<Weather>
    fun saveEntity(weather: Weather)
}