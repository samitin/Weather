package ru.samitin.weather.model.repository


import ru.samitin.weather.model.dto.WeatherDTO
import ru.samitin.weather.model.retrofit.RemoteDataSource

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource): DetailsRepository {
    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }
}
