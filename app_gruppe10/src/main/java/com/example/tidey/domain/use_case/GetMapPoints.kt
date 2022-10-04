package com.example.tidey.domain.use_case

import com.example.tidey.domain.repository.MapRepository

class GetMapPoints (
    private val repo: MapRepository
) {
    operator fun invoke() = repo.getMapPointsFromFirestore()
}