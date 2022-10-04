package com.example.tidey.domain.use_case

data class UseCases (
    val getMapPoints: GetMapPoints,
    val addMapPoint: AddMapPoint,
    val deleteMapPoint: DeleteMapPoint
)