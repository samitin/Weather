package ru.samitin.weather.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ru.samitin.weather.R
import ru.samitin.weather.databinding.WeatherFragmentBinding
import ru.samitin.weather.model.Weather
import ru.samitin.weather.viewmodel.AppState
import ru.samitin.weather.viewmodel.MainViewModel

class DetailsFragment : Fragment() {


    companion object {
        const val WEATHER_KEY="WEATHER_KEY"
        fun newInstance(weather:Weather):DetailsFragment{
            val fragment= DetailsFragment()
            val bundle=Bundle()
            bundle.putParcelable(WEATHER_KEY,weather)
            fragment.arguments=bundle
            return fragment
        }
    }

    private var _binding:WeatherFragmentBinding? =null
    private val binding:WeatherFragmentBinding
    get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding= WeatherFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData(arguments?.getParcelable<Weather>(WEATHER_KEY) as Weather)
    }
    private fun setData(weatherData: Weather) {
        binding.cityName.text = weatherData.city.city
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.lat.toString(),
            weatherData.city.lon.toString()
        )
        binding.temperatureValue.text = weatherData.temperature.toString()
        binding.feelsLikeValue.text = weatherData.feelsLike.toString()
    }

    override fun onDestroyView() {
        _binding=null;
        super.onDestroyView()
    }
}