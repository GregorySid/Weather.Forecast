package com.example.wea23

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wea23.ViewM.MainVM
import com.example.wea23.ViewST.Data
import com.example.wea23.ViewST.Error
import com.example.wea23.ViewST.Loading
import com.example.wea23.ViewST.MainVSt
import com.example.wea23.adapter.FutureAdapter
import com.example.wea23.adapter.WeaAdapter
import com.example.wea23.data_model.ForecastOneDay
import com.example.wea23.data_model.WeatherDataM
import com.example.wea23.databinding.FragmentOneBinding
import com.example.wea23.extens.DialogM
import com.example.wea23.extens.isPermGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class Frag_one : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    FutureAdapter.Listner {
    private lateinit var binding: FragmentOneBinding
    private var addList: List<Address>? = null
    private lateinit var mMap: GoogleMap
    private var city: String = "London"
    private lateinit var adapter: WeaAdapter
    private lateinit var _adapter: FutureAdapter
    private lateinit var pLaunch: ActivityResultLauncher<String>
    private lateinit var fLocClient: FusedLocationProviderClient
    private val viewModel: MainVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fLocClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFrag.getMapAsync(this)
        rcView()
        rc_ViewDay()
        checkPerm()

        lifecycle.coroutineScope.launch {
            viewModel.viewState
                .flowWithLifecycle(lifecycle)
                .collect { state ->
                    renderState(state)
                }
        }

        binding.SweipL.setOnRefreshListener {
            checkLoc()
        }

        binding.sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                if (text != null) {
                    city = text
                    val geo = Geocoder(requireContext())
                    try {
                        addList = geo.getFromLocationName(text, 1)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                val addr = addList!![0]
                val lattt = LatLng(addr.latitude, addr.longitude)
                markerOnMap(lattt)
                viewModel.loadWeather(city)
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        checkLoc()
    }

    private fun renderState(state: MainVSt) {
        when (state) {
            is Data -> renderData(state.weatherDataM)
            is Error -> renderError()
            is Loading -> renderLoading()
        }
    }

    private fun rcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        adapter = WeaAdapter()
        rcView.adapter = adapter
    }

    private fun rc_ViewDay() = with(binding) {
        rcDays.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        _adapter = FutureAdapter(this@Frag_one)
        rcDays.adapter = _adapter
    }

    private fun renderData(weatherDataM: WeatherDataM) = with(binding) {
        val timeDate: Date? =
            SimpleDateFormat("yyyy-MM-dd hh:mm").parse(weatherDataM.location.localtime)
        val humanTime: String? =
            timeDate?.let { SimpleDateFormat("d MMMM '|' H:mm").format(it) }
        val today = weatherDataM.forecast.forecastday[0]
        val hours = today.hour
        val todays = weatherDataM.forecast.forecastday
        val days = todays
        val maxMin = "H: ${today.day.maxtemp_c}  L: ${today.day.mintemp_c}"
        SweipL.isRefreshing = false
        tv21.text = getString(R.string.rain_p, weatherDataM.current.rain)
        tv31.text = getString(R.string.wind_p, weatherDataM.current.wind)
        iv41.text = "${weatherDataM.current.humidity}%"
        textView.text = weatherDataM.location.name
        textView1.text = weatherDataM.current.condition.text
        textView2.text = humanTime
        textView3.text = getString(R.string.temp, weatherDataM.current.temp_c)
        textView4.text = maxMin
        Picasso.get().load("https:" + weatherDataM.current.condition.icon).into(binding.cloudy)
        adapter.submitList(hours)
        _adapter.submitList(days)
    }

    private fun renderError() {
        binding.SweipL.isRefreshing = false
        Toast.makeText(requireContext(), getString(R.string.error_text), Toast.LENGTH_SHORT).show()
    }

    private fun renderLoading() {
        binding.SweipL.isRefreshing = false
    }

    private fun checkLoc() {
        binding.SweipL.isRefreshing = false
        if (locEnabled()) {
            getLoc()
        } else {
            DialogM.locDialog(requireContext(), object : DialogM.Listn {
                override fun onClick() {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
        }
    }

    private fun locEnabled(): Boolean {
        val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLoc() {
        if (!locEnabled()) {
            return
        }
        val cToken = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cToken.token)
            .addOnCompleteListener {
                viewModel.loadWeather("${it.result.latitude}, ${it.result.longitude}")
                val lon = LatLng(it.result.latitude, it.result.longitude)
                markerOnMap(lon)
            }
    }

    private fun markerOnMap(lon: LatLng) {
        val marker = MarkerOptions().position(lon)
        marker.title("$lon")
        mMap.addMarker(marker)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lon, 11f))
    }

    override fun onMapReady(google: GoogleMap) {
        mMap = google
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        getLoc()
    }

    override fun onMarkerClick(m: Marker) = false

    private fun permListner() {
        pLaunch = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(requireContext(), "Permission is $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPerm() {
        if (!isPermGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permListner()
            pLaunch.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Frag_one()
    }

    override fun onClicke(item: ForecastOneDay) {
        findNavController().navigate(R.id.action_frag_one_to_frag_two)

    }
}