package com.example.mobil.sorgular.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.sorgular.R
import com.example.mobil.sorgular.model.TripData
import java.sql.Date

class Sorgu1Adapter(private val dataSet: List<TripData>) :
    RecyclerView.Adapter<Sorgu1Adapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtNumber: TextView
        var txtDay: TextView
        var txtDistance: TextView
        var txtPassengerCount: TextView

        init {
            txtNumber = view.findViewById(R.id.txtNumber)
            txtDay = view.findViewById(R.id.txtDay)
            txtDistance = view.findViewById(R.id.txtDistance)
            txtPassengerCount = view.findViewById(R.id.txtPassengerCount)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_sorgu_1, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.txtNumber.text = (position+1).toString()
        viewHolder.txtDay.text = Date((dataSet.get(position).tpep_pickup_datetime*1000)+10800000).toString()
        viewHolder.txtDistance.text = dataSet.get(position).trip_distance.toString() + "mil"
        viewHolder.txtPassengerCount.text = dataSet.get(position).passenger_count.toString() +" Yolcu"
    }

    override fun getItemCount() = dataSet.size

}