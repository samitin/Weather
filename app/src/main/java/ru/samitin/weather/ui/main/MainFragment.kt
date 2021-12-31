package ru.samitin.weather.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import ru.samitin.weather.R
import ru.samitin.weather.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }
    private var _binding:MainFragmentBinding? =null
    private val binding:MainFragmentBinding
    get() = _binding!!


    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding= MainFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer=Observer<Any>{renderDta(it)}
        viewModel.getData().observe(viewLifecycleOwner,observer)
    }
    private fun renderDta(data:Any){
        Toast.makeText(context,"Data",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding=null;
        super.onDestroyView()
    }
}