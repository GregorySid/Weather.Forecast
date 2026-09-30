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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wea23.ViewM.MainVM
import com.example.wea23.extens.DialogM
import com.example.wea23.extens.isPermGranted
import com.example.wea23.ui.screens.main.WeatherScreen
import com.example.wea23.ui.theme.Wea23Theme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource

class Frag_one : Fragment() {
    private val viewModel: MainVM by activityViewModels()

    /** Позиция карты: из геолокации или поиска; читается в WeatherMap */
    private val mapPosition = mutableStateOf<LatLng?>(null)
    private var addList: List<Address>? = null
    private lateinit var fLocClient: FusedLocationProviderClient
    private lateinit var pLaunch: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fLocClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Wea23Theme {
                    WeatherScreen(
                        viewModel = viewModel,
                        mapPosition = mapPosition,
                        onDayClick = { item ->
                            viewModel.m.value = item
                            findNavController().navigate(R.id.action_frag_one_to_frag_two)
                        },
                        onCitySearch = ::searchCity,
                        onRefresh = ::checkLoc
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPerm()
    }

    override fun onResume() {
        super.onResume()
        checkLoc()
    }

    /** Поиск города: геокодинг + маркер на карте + загрузка погоды */
    private fun searchCity(city: String) {
        val geo = Geocoder(requireContext())
        try {
            addList = geo.getFromLocationName(city, 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        addList?.firstOrNull()?.let { addr ->
            mapPosition.value = LatLng(addr.latitude, addr.longitude)
        }
        viewModel.loadWeather(city)
    }

    private fun checkLoc() {
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
                mapPosition.value = LatLng(it.result.latitude, it.result.longitude)
            }
    }

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
}
