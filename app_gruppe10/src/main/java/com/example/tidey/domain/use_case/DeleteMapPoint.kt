package com.example.tidey.domain.use_case

import com.example.tidey.domain.repository.MapRepository

class DeleteMapPoint(
    private val repo: MapRepository
) {
    suspend operator fun invoke(id: String) = repo.deleteMapPointFromFirestore(id)
}