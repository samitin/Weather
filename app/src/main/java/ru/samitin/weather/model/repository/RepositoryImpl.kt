package ru.samitin.weather.model.repository

import ru.samitin.weather.model.data.Weather
import ru.samitin.weather.model.data.getRussianCities
import ru.samitin.weather.model.data.getWorldCities

class RepositoryImpl : Repository {

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorageRus():List<Weather> = getRussianCities()


    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getWorldCities()
}