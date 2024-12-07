package com.academiaaiapaec.appasistenciaaiapaec.network

import com.academiaaiapaec.appasistenciaaiapaec.model.response.response
import retrofit2.Response
import retrofit2.http.GET

interface SedeService {
    @GET("public")
    suspend fun getSedes(): Response<response>

}