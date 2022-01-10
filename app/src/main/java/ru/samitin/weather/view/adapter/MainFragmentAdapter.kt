package ru.samitin.weather.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.samitin.weather.R
import ru.samitin.weather.model.Weather

class MainFragmentAdapter :RecyclerView.Adapter<MainFragmentAdapter.MainFragmentHolder>(){

    private var weatherData= listOf<Weather>()
    private var onClickWeaterListener:OnClickWeaterListener ? =null
    fun setWeather(data : List<Weather>){
        weatherData=data
        notifyDataSetChanged()
    }

     fun setOnClickWeaterListener(onClickWeaterListener:OnClickWeaterListener){
         this.onClickWeaterListener=onClickWeaterListener
     }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_recycler_item,parent,false) as View
        return MainFragmentHolder(view)
    }

    override fun onBindViewHolder(holder: MainFragmentHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int = weatherData.size

    inner class MainFragmentHolder(view : View): RecyclerView.ViewHolder(view){
        fun bind(weather: Weather){
            var city= itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView)
            city.text=weather.city.city
            city.setOnClickListener {
                    Toast.makeText(itemView.context,weather.city.city,Toast.LENGTH_SHORT).show()
                onClickWeaterListener?.onClick(weather)
                }

        }
    }
    interface OnClickWeaterListener{
        fun onClick(weather:Weather)
    }
}