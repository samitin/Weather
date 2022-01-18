package ru.samitin.weather.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_history_recycler_item.view.*
import ru.samitin.weather.R
import ru.samitin.weather.model.data.Weather

class HistoryAdapter :RecyclerView.Adapter<HistoryAdapter.RecyclerViewHolder>() {

    private var data:List<Weather> = arrayListOf()

    fun setData(data: List<Weather>){
        this.data = data
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_history_recycler_item,parent,false) as View)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
    inner class RecyclerViewHolder(view:View) :RecyclerView.ViewHolder(view){
        fun bind(data: Weather){
            if (layoutPosition != RecyclerView.NO_POSITION){
                itemView.recyclerViewItem.text= String.format("%s %d %s",data.city.city,data.temperature,data.condition)
                itemView.setOnClickListener {
                    Toast.makeText(itemView.context,"on click: ${data.city.city}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}