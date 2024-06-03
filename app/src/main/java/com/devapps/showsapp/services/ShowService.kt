package com.devapps.showsapp.services

import com.devapps.showsapp.ShowResponse
import retrofit2.Response
import retrofit2.http.GET

interface ShowService {
    @GET("most-popular?page=1")
    suspend fun getShows(): Response<ShowResponse>
}