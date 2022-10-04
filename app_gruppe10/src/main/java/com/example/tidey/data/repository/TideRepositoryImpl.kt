package com.example.tidey.data.repository

import com.example.tidey.data.remote.TideApi
import com.example.tidey.domain.repository.TideRepository
import okhttp3.ResponseBody
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class TideRepositoryImpl @Inject constructor(
    private val tideApi: TideApi,
): TideRepository {
    override suspend fun downloadXml(data: Map<String, String>): Response<ResponseBody> {
        return tideApi.downloadXml(data)
    }

    override fun getCurrentDate(plusDays: Long, format: String): String {
            val current = LocalDateTime.now().plusDays(plusDays)
            val formatter = DateTimeFormatter.ofPattern(format)
            return current.format(formatter)
    }
}