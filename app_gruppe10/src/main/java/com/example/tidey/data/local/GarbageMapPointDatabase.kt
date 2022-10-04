package com.example.tidey.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GarbageMapPointEntity::class],
    version = 1
)
abstract class GarbageMapPointDatabase: RoomDatabase() {
    abstract val dao: GarbageMapPointDao
}