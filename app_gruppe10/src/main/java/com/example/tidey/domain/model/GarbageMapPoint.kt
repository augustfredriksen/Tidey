package com.example.tidey.domain.model


data class GarbageMapPoint(
    var lat: Double? = null,
    var lng: Double? = null,
    var garbageLevel: String? = null,
    var user: String? = null,
    var dateReported: String? = null,
    var id: String? = null
)