package com.academiaaiapaec.appasistenciaaiapaec.network

import com.academiaaiapaec.appasistenciaaiapaec.model.response.TrabajadorResponse
import com.academiaaiapaec.appasistenciaaiapaec.model.response.response
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TrabajadorService {
    @GET("public/{id_trabajador}")
    suspend fun getTrabajador(@Path("id_trabajador") trabajadorId: Int): Response<TrabajadorResponse>
}