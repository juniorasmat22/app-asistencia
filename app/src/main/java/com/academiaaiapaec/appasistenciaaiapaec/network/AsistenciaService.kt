package com.academiaaiapaec.appasistenciaaiapaec.network

import com.academiaaiapaec.appasistenciaaiapaec.model.Asistencia
import com.academiaaiapaec.appasistenciaaiapaec.model.response.AsistenciaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AsistenciaService {
    @POST("public/registro")
    suspend fun registrarAsistencia(@Body asistencia: Asistencia): Response<AsistenciaResponse>
}
