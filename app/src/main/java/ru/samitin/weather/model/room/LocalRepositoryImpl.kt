package ru.samitin.weather.model.room

import ru.samitin.weather.model.data.Weather
import ru.samitin.weather.utils.convertHistoryEntityToWeather
import ru.samitin.weather.utils.convertWeatherToEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDao):LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}