package ru.samitin.weather.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.samitin.weather.R
import ru.samitin.weather.databinding.FragmentMainBinding
import ru.samitin.weather.model.data.Weather
import ru.samitin.weather.view.adapter.MainFragmentAdapter
import ru.samitin.weather.viewmodel.AppState
import ru.samitin.weather.viewmodel.MainViewModel

private const val IS_WORLD_KEY = "LIST_OF_TOWNS_KEY"
class MainFragment : Fragment() {
    private var isDataSetWorld:Boolean=false
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private val adapter = MainFragmentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()
        showListOfTowns()
    }
    private fun showListOfTowns(){
        activity?.let {
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY,false)){
                changeWeatherDataSet()
            }else{
                viewModel.getWeatherFromLocalSourceRus()
            }
        }
    }

    private fun changeWeatherDataSet() {
        if (isDataSetWorld) {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }
        isDataSetWorld = !isDataSetWorld
        saveListOfTowns(isDataSetWorld)
    }

    private fun saveListOfTowns(dataSetWorld: Boolean) {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()){
                putBoolean(IS_WORLD_KEY,isDataSetWorld)
                apply()
            }
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
                adapter.setOnClickWeaterListener(object :MainFragmentAdapter.OnClickWeaterListener{
                    override fun onClick(weather: Weather) {
                        val bundle=Bundle()
                        bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA,weather)
                        activity?.supportFragmentManager?.beginTransaction()?.
                                addToBackStack(null)
                            ?.add(R.id.container,DetailsFragment.newInstance(bundle))
                            ?.commit()
                    }
                })
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainFragmentFAB, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) { viewModel.getWeatherFromLocalSourceRus() }
                    .show()
            }
        }
    }

    companion object {
        fun newInstance() =
            MainFragment()
    }
}