package com.example.wea23.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wea23.R
import com.example.wea23.data_model.ForecastOneDay
import com.example.wea23.databinding.ViewHolderDayBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class FutureAdapter(val listner: Listner?): ListAdapter<ForecastOneDay, FutureAdapter.Holder>(Compl()) {

    class Holder(view: View, val listner: Listner?): RecyclerView.ViewHolder(view){
        private val binding = ViewHolderDayBinding.bind(view)
        private var itemTo: ForecastOneDay? = null

        init {
            itemView.setOnClickListener {
                itemTo?.let { it1 -> listner?.onClicke(it1) }
            }
        }

        fun bind(item: ForecastOneDay) = with(binding){
            itemTo = item
            val timeDate: Date =
                SimpleDateFormat("yyyy-MM-dd").parse(item.date)
            val humanTime: String =
                SimpleDateFormat("d MMMM").format(timeDate)
            tvDat.text = humanTime
            tvMax.text = String.format("%.0f℃", item.day.maxtemp_c)
            tvMin.text =  String.format("%.0f℃", item.day.mintemp_c)
            tvStatu.text = item.day.condition.text
            Picasso.get().load("https:" + item.day.condition.icon).into(ivDay)
        }
    }

    class Compl: DiffUtil.ItemCallback<ForecastOneDay>(){
        override fun areItemsTheSame(oldItem: ForecastOneDay, newItem: ForecastOneDay): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ForecastOneDay, newItem: ForecastOneDay): Boolean {
            return  oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_day, parent, false)
        return Holder(view, listner)
    }

    override fun onBindViewHolder(holder: Holder, position: Int){
        holder.bind(getItem(position))
    }

    interface Listner{
        fun onClicke(item: ForecastOneDay)
    }
}