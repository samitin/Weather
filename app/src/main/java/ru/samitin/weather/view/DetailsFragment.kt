package ru.samitin.weather.view


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import ru.samitin.weather.R
import ru.samitin.weather.databinding.FragmentDetailsBinding
import ru.samitin.weather.model.retrofit.WeatherLoader
import ru.samitin.weather.model.data.Weather
import ru.samitin.weather.model.dto.WeatherDTO
import ru.samitin.weather.viewmodel.DetailsViewModel


const val YOUR_API_KEY="4d955e52-f675-474f-b36b-5b579525ec73"
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private val viewModel : DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }
    private val onLoadListener: WeatherLoader.WeatherLoaderListener =
        object : WeatherLoader.WeatherLoaderListener {

            override fun onLoaded(weatherDTO: WeatherDTO) {
                displayWeather(weatherDTO)
            }

            override fun onFailed(throwable: Throwable) {
                //Обработка ошибки
            }
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
    : View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        viewModel.saveCityToDB(weatherBundle)
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        val loader = WeatherLoader(onLoadListener, weatherBundle.city.lat, weatherBundle.city.lon)
        loader.loadWeather()
    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = weatherBundle.city
            cityName.text = city.city
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            weatherCondition.text = weatherDTO.fact?.condition
            temperatureValue.text = weatherDTO.fact?.temp.toString()
            feelsLikeValue.text = weatherDTO.fact?.feels_like.toString()
        }
    }

    companion object {

        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}