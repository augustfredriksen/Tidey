package com.example.tidey.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

@Entity(indices = [Index(value = ["lat", "lng"],
    unique = true)])
data class GarbageMapPointEntity(
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lng") val lng: Double,
    val garbageLevel: String,
    val user: String,
    val dateReported: String,
    @Nonnull @PrimaryKey val id: String
)