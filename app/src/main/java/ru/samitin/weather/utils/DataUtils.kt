package ru.samitin.weather.utils


import ru.samitin.weather.model.data.Weather
import ru.samitin.weather.model.data.getDefaultCity
import ru.samitin.weather.model.dto.FactDto
import ru.samitin.weather.model.dto.WeatherDTO

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: FactDto = weatherDTO.fact!!
    return listOf(Weather(getDefaultCity(), fact.temp!!, fact.feels_like!!, fact.condition!!, fact.icon))
}