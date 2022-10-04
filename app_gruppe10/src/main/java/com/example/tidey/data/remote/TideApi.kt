package com.example.tidey.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Streaming

interface TideApi {
    @Streaming
    @GET("tideapi.php")
    suspend fun downloadXml(
        @QueryMap data: Map<String, String>
    ): Response<ResponseBody>
}