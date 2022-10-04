package com.example.tidey.domain.repository

import com.example.tidey.domain.model.GarbageMapPoint
import com.example.tidey.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface MapRepository {

    suspend fun insertMap(spot: GarbageMapPoint)

    suspend fun deleteMap(spot: GarbageMapPoint)

    suspend fun getMarkerById(id: String): GarbageMapPoint?

    fun getMap(): Flow<List<GarbageMapPoint>>

    fun getMapPointsFromFirestore(): Flow<Response<List<GarbageMapPoint>>>

    suspend fun addMapPointsToFirestore(
        lat: Double,
        lng: Double,
        garbageLevel: String,
        user: String,
        dateReported: String,
        id: String
    ): Flow<Response<Void?>>

    suspend fun deleteMapPointFromFirestore(id: String): Flow<Response<Void?>>
}