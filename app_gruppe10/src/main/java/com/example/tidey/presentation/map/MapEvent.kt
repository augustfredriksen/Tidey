package com.example.tidey.presentation.map

import com.example.tidey.domain.model.GarbageMapPoint
import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {
    object ToggleDarkMode: MapEvent()
    data class InsertGarbageMapPoint(val latLng: LatLng) : MapEvent()
    data class OnMapClick(val latLng: LatLng) : MapEvent()
    data class OnInfoWindowClick(val spot: GarbageMapPoint) : MapEvent()
    data class OnInfoWindowLongClick(val spot: GarbageMapPoint) : MapEvent()
}