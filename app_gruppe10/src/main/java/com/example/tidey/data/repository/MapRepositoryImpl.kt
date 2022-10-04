package com.example.tidey.data.repository

import com.example.tidey.data.local.GarbageMapPointDao
import com.example.tidey.data.local.toGarbageMapPoint
import com.example.tidey.data.local.toGarbageMapPointEntity
import com.example.tidey.domain.model.GarbageMapPoint
import com.example.tidey.domain.model.Response
import com.example.tidey.domain.repository.MapRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapRepositoryImpl @Inject constructor(
    private val mapPointRef: CollectionReference,
    private val dao: GarbageMapPointDao
): MapRepository {

    override suspend fun insertMap(spot: GarbageMapPoint) {
        dao.insertMap(spot.toGarbageMapPointEntity())
    }

    override suspend fun deleteMap(spot: GarbageMapPoint) {
        dao.deleteMap(spot.toGarbageMapPointEntity())
    }


    override suspend fun getMarkerById(id: String): GarbageMapPoint? {
        return dao.getMarkerById(id)?.toGarbageMapPoint()
    }

    override fun getMap(): Flow<List<GarbageMapPoint>> {
        return dao.getMap().map { spots ->
            spots.map { it.toGarbageMapPoint() }
        }
    }

    override fun getMapPointsFromFirestore() = callbackFlow {
        val snapshotListener = mapPointRef.orderBy("id").addSnapshotListener { snapshot, e ->
            val response = if (snapshot != null) {
                val garbageMapPointPoints = snapshot.toObjects(GarbageMapPoint::class.java)
                Response.Success(garbageMapPointPoints)
            } else {
                Response.Failure(e)
            }
            trySend(response).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addMapPointsToFirestore(
        lat: Double,
        lng: Double,
        garbageLevel: String,
        user: String,
        dateReported: String,
        id: String
    ): Flow<Response<Void?>> = flow {
        try {
            emit(Response.Loading)
            val garbageMapPointPoint = GarbageMapPoint(
                id = id,
                lat = lat,
                lng = lng,
                garbageLevel = garbageLevel,
                user = user,
                dateReported = dateReported
            )
            val addition = mapPointRef.document(lat.toString() + lng.toString()).set(garbageMapPointPoint).await()
            emit(Response.Success(addition))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun deleteMapPointFromFirestore(Id: String) = flow {
        try {
            emit(Response.Loading)
            val deletion = mapPointRef.document(Id).delete().await()
            emit(Response.Success(deletion))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }
}