package com.example.tidey.domain.use_case

import com.example.tidey.domain.repository.MapRepository

class AddMapPoint(
    private val repo: MapRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lng: Double,
        garbageLevel: String,
        user: String,
        dateReported: String,
        id: String
    ) = repo.addMapPointsToFirestore(lat, lng, garbageLevel, user, dateReported, id)
}