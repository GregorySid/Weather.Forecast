package com.example.wea23.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

/** Замена SupportMapFragment + onMapReady/markerOnMap из Frag_one */
@Composable
fun WeatherMap(mapPosition: State<LatLng?>, modifier: Modifier = Modifier) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 2f)
    }

    LaunchedEffect(mapPosition.value) {
        mapPosition.value?.let { pos ->
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(pos, 11f))
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = remember { MapUiSettings(zoomControlsEnabled = true) }
    ) {
        mapPosition.value?.let { pos ->
            Marker(state = MarkerState(position = pos), title = "$pos")
        }
    }
}
