package ru.samitin.weather.model

class RepositoryImpl : Repository {

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorageRus():List<Weather> = getRussianCities()


    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getWorldCities()
}