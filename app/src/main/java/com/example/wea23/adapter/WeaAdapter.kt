package com.example.wea23.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wea23.R
import com.example.wea23.data_model.ForecastForHour
import com.example.wea23.databinding.ViewHolderBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class WeaAdapter: ListAdapter<ForecastForHour, WeaAdapter.Holder>(Comp()) {

    class Holder(view: View): RecyclerView.ViewHolder(view){
        private val binding = ViewHolderBinding.bind(view)

        fun bind(item: ForecastForHour) = with(binding){
            val timeDate: Date? =
                SimpleDateFormat("yyyy-MM-dd hh:mm").parse(item.time)
            val humanTime: String? =
                timeDate?.let { SimpleDateFormat("H a").format(it) }
            tvHour.text = humanTime
            tvTemp.text = String.format("%.0f℃", item.temp_c)
            Picasso.get().load("https:" + item.condition.icon).into(ivPic)
        }
    }

    class Comp: DiffUtil.ItemCallback<ForecastForHour>(){
        override fun areItemsTheSame(oldItem: ForecastForHour, newItem: ForecastForHour): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ForecastForHour, newItem: ForecastForHour): Boolean {
            return  oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}