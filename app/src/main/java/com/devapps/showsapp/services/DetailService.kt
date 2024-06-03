package com.devapps.showsapp.services

import com.devapps.showsapp.DetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DetailService {
    @GET("show-details")
    suspend fun getShowDetails (@Query("q") showId: String): Response<DetailResponse>
}