package com.example.tidey.presentation.map

import com.example.tidey.domain.model.GarbageMapPoint
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(),
    val garbageMapPoint: List<GarbageMapPoint> = emptyList(),
    val marker: GarbageMapPoint? = null,
    val isDarkMap: Boolean = false
)