package com.example.tidey.data.local

import com.example.tidey.domain.model.GarbageMapPoint

fun GarbageMapPointEntity.toGarbageMapPoint(): GarbageMapPoint {
    return GarbageMapPoint(
        lat = lat,
        lng = lng,
        garbageLevel = garbageLevel,
        user = user,
        dateReported = dateReported,
        id = id
    )
}

fun GarbageMapPoint.toGarbageMapPointEntity(): GarbageMapPointEntity {
    return GarbageMapPointEntity(
        lat = lat!!,
        lng = lng!!,
        garbageLevel = garbageLevel!!,
        user = user!!,
        dateReported = dateReported!!,
        id = id!!
    )
}