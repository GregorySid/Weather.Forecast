package com.example.wea23

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.example.wea23.ViewM.MainVM
import com.example.wea23.ViewST.Data
import com.example.wea23.ViewST.Error
import com.example.wea23.ViewST.Loading
import com.example.wea23.ViewST.MainVSt
import com.example.wea23.data_model.WeatherDataM
import com.example.wea23.databinding.FragmentTwoBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class Frag_two : Fragment(){
    private lateinit var binding: FragmentTwoBinding
    private val viewModel: MainVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       ini()

        lifecycle.coroutineScope.launch {
            viewModel.viewState
                .flowWithLifecycle(lifecycle)
                .collect { state ->
                    rState(state)
                }
        }
    }

    private fun rState(state: MainVSt){
        when (state){
            is Data -> rData(state.weatherDataM)
            is Error -> rError()
            is Loading -> rLoading()
        }
    }

    private fun rData(weatherDataM: WeatherDataM) = with(binding) {
        val today = weatherDataM.forecast.forecastday[0]
        val t = today.day.condition.icon
        val timeDate: Date =
            SimpleDateFormat("yyyy-MM-dd").parse(today.date)
        val humanTime: String =
            SimpleDateFormat("d MMMM").format(timeDate)
//        val maxMin = "${today.day.maxtemp_c} ${today.day.mintemp_c}"
        textView5.text = humanTime
        textView8.text = String.format("%.0f℃", today.day.maxtemp_c)
        textView888.text = String.format("%.0f℃", today.day.mintemp_c)
        textView9.text = today.day.condition.text
        Picasso.get().load("https:$t").into(binding.imageView2)
        tv21.text = getString(R.string.rain_p, today.day.rain)
        tv31.text = getString(R.string.wind_p, today.day.wind)
        iv41.text = "${today.day.wind}%"
        tvIi.text = today.astro.sunrise
        tvPp.text = today.astro.sunset
        tv33.text = today.astro.moonrise
        tv66.text = today.astro.moonset
        tv5.text = today.astro.moon_phase
    }

    private fun rError() {
        Toast.makeText(requireContext(), getString(R.string.error_text), Toast.LENGTH_SHORT).show()
    }

    private fun rLoading() {
    }

    private fun ini() = with(binding){
        backOn.setOnClickListener {
            findNavController().navigate(R.id.action_frag_two_to_frag_one)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Frag_two()
    }

}