package com.example.tidey.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GarbageMapPointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMap(spot: GarbageMapPointEntity)

    @Delete
    suspend fun deleteMap(spot: GarbageMapPointEntity)

    @Query("SELECT * FROM garbagemappointentity WHERE id = :id")
    fun getMarkerById(id: String): GarbageMapPointEntity?

    @Query("SELECT * FROM garbagemappointentity WHERE lat = :lat & lng = :lng")
    fun getMarkerByLatLng(lat: Double, lng: Double): GarbageMapPointEntity?

    @Query("SELECT * FROM garbagemappointentity")
    fun getMap(): Flow<List<GarbageMapPointEntity>>
}
