package ru.samitin.weather.experement

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.fragment_threads.*
import ru.samitin.weather.R
import ru.samitin.weather.databinding.FragmentThreadsBinding
import java.util.*
import java.util.concurrent.TimeUnit


class ThreadsFragment : Fragment() {

    private var _binding: FragmentThreadsBinding? = null
    private val binding get() = _binding!!
    private var counterThread = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        binding.button.setOnClickListener {
            binding.textView.text = startCalculations(binding.editText.text.toString().toInt())
            binding.mainContainer.addView(AppCompatTextView(it.context).apply {
                text = getString(R.string.in_main_thread)
                textSize = resources.getDimension(R.dimen.main_container_text_size)
            })
        }
        //
        binding.calcThreadBtn.setOnClickListener {
            Thread {
                counterThread++
                val calculatedText = startCalculations(editText.text.toString().toInt())
                activity?.runOnUiThread {
                    binding.textView.text = calculatedText
                    binding.mainContainer.addView(AppCompatTextView(it.context).apply {
                        text = String.format(getString(R.string.from_thread), counterThread)
                        textSize = resources.getDimension(R.dimen.main_container_text_size)
                    })
                }

            }.start()
        }
        //
        val handlerThread = HandlerThread(getString(R.string.my_handler_thread))
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        calcThreadHandler.setOnClickListener {
            mainContainer.addView(AppCompatTextView(it.context).apply {
                text = String.format(
                    getString(R.string.calculate_in_thread),
                    handlerThread.name
                )
                textSize = resources.getDimension(R.dimen.main_container_text_size)
            })

            handler.post {
                startCalculations(binding.editText.text.toString().toInt())
                mainContainer.post {
                    mainContainer.addView(AppCompatTextView(it.context).apply {
                        text = String.format(
                            getString(R.string.calculate_in_thread),
                            Thread.currentThread().name
                        )
                        textSize = resources.getDimension(R.dimen.main_container_text_size)
                    })
                }
            }
        }
        //
        initServiceButton()
    }

    private fun initServiceButton(){
        binding.serviceButton.setOnClickListener {
            context?.let {
                it.startService(Intent(it,MainService::class.java).apply {
                    putExtra(
                        MAIN_SERVICE_STRING_EXTRA,
                        getString(R.string.hello_from_thread_fragment)
                    )
                })
            }
        }
    }

    private fun startCalculations(seconds: Int): String {
        val date = Date()
        var diffInSec: Long
        do {
            val currentDate = Date()
            val diffInMs: Long = currentDate.time - date.time
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs)
        } while (diffInSec < seconds)
        return diffInSec.toString()
    }

    companion object {
        fun newInstance() = ThreadsFragment()
    }
}