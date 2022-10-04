package com.example.tidey.domain.repository

import okhttp3.ResponseBody
import retrofit2.Response

interface TideRepository {
    suspend fun downloadXml(data: Map<String, String>): Response<ResponseBody>

    fun getCurrentDate(plusDays: Long = 0, format: String): String


}