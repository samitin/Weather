package ru.samitin.weather.model


import ru.samitin.weather.model.dto.WeatherDTO

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource):DetailsRepository {
    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }
}
