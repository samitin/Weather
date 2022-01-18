package ru.samitin.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_history.*
import ru.samitin.weather.R
import ru.samitin.weather.databinding.FragmentHistoryBinding
import ru.samitin.weather.utils.showSnackBar
import ru.samitin.weather.view.adapter.HistoryAdapter
import ru.samitin.weather.viewmodel.AppState
import ru.samitin.weather.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {
    private var _binding:FragmentHistoryBinding? =null
    private val binding get() = _binding!!
    private val viewModel:HistoryViewModel by lazy { ViewModelProvider(this).get(HistoryViewModel::class.java) }
    private val adapter:HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyFragmentRecyclerview.adapter=adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getAllHistory()
    }

    private fun renderData(appState: AppState?) {
        when(appState){
            is AppState.Success -> {
                binding.historyFragmentRecyclerview.visibility=View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                adapter.setData(appState.weatherData)
            }
            is AppState.Loading ->{
                binding.historyFragmentRecyclerview.visibility=View.GONE
                binding.includedLoadingLayout.loadingLayout.visibility=View.VISIBLE
            }
            is AppState.Error ->{
                binding.historyFragmentRecyclerview.visibility=View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.historyFragmentRecyclerview.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        viewModel.getAllHistory()
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
    companion object{
        @JvmStatic
        fun newInstance()=HistoryFragment()
    }
}