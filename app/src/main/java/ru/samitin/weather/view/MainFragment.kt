package ru.samitin.weather.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import ru.samitin.weather.R
import ru.samitin.weather.databinding.FragmentMainBinding
import ru.samitin.weather.model.data.City
import ru.samitin.weather.model.data.Weather
import ru.samitin.weather.view.adapter.MainFragmentAdapter
import ru.samitin.weather.viewmodel.AppState
import ru.samitin.weather.viewmodel.MainViewModel
import java.io.IOException

private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f
private const val IS_WORLD_KEY = "LIST_OF_TOWNS_KEY"
class MainFragment : Fragment() {
    private var isDataSetWorld:Boolean=false
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnClickWeaterListener{
        override fun onClick(weather: Weather) {
            openDitailsFragment(weather)
        }
    })

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
        binding.mainFragmentFABLocation.setOnClickListener { checkPermission() }
        showListOfTowns()
    }

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it,Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED ->{
                            getLocation()
                        }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ->{
                    showRationaleDialog()
                }
                else -> {requestPermossion()}
            }
        }
    }

    private fun requestPermossion() {

        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            Companion.REQUEST_CODE
        )
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_rationale_meaasge))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access)){_,_ ->
                    requestPermossion()
                }
                .setNegativeButton(getString(R.string.dialog_rationale_decline)){dialog,_ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun getLocation() {
        activity?.let {context ->
            if (ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED){
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                }else{
                    val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null){
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    }else{
                        getAddressAsync(context,location)
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            }else{showRationaleDialog()}
        }
    }
    private val onLocationListener = object  : LocationListener{
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddressAsync(it,location)
            }
        }
    }
    private fun getAddressAsync(context: Context,location: Location){
        val geoCoder = Geocoder(context)
        Thread{
            try {
                val  addresses = geoCoder.getFromLocation(location.latitude,location.longitude,1)
                mainFragmentFAB.post {
                    showAddressDialog(addresses[0].getAddressLine(0),location)
                }
            }catch (e: IOException){
                e.printStackTrace()
            }
        }.start()
    }
    private fun showAddressDialog(address: String,location: Location){
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)){_,_ ->
                    openDitailsFragment(
                        Weather(
                            City(address,location.latitude,location.longitude)
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)){dialog,_ ->
                    dialog.dismiss()
                }.create()
                .show()
        }
    }

    private fun openDitailsFragment(weather: Weather) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .add(R.id.container,
                DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(DetailsFragment.BUNDLE_EXTRA,weather)
                }))
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        checkPermissionsResult(requestCode, grantResults)
    }

    private fun checkPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                var grantedPermissions = 0
                if ((grantResults.isNotEmpty())) {
                    for (i in grantResults) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions++
                        }
                    }
                    if (grantResults.size == grantedPermissions) {
                        getLocation()
                    } else {
                        showDialog(
                            getString(R.string.dialog_title_no_gps),
                            getString(R.string.dialog_message_no_gps)
                        )
                    }
                } else {
                    showDialog(
                        getString(R.string.dialog_title_no_gps),
                        getString(R.string.dialog_message_no_gps)
                    )
                }
                return
            }
        }
    }

    private fun showDialog(title: String, massage: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(massage)
                .setNegativeButton(getString(R.string.dialog_button_close)){dialog, _ ->
                    dialog.dismiss()
                }.create()
                .show()
        }
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

        const val REQUEST_CODE=25
    }
}