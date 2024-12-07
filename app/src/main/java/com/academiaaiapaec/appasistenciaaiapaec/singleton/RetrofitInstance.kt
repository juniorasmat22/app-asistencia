package com.academiaaiapaec.appasistenciaaiapaec.singleton

import com.academiaaiapaec.appasistenciaaiapaec.network.AsistenciaService
import com.academiaaiapaec.appasistenciaaiapaec.network.SedeService
import com.academiaaiapaec.appasistenciaaiapaec.network.TrabajadorService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // URL base para el servicio de Asistencia
    private const val BASE_URL_ASISTENCIA = "https://www.api.academiaaiapaec.edu.pe/api/asistencia-trabajador/"

    // URL base para el servicio de Sedes
    private const val BASE_URL_SEDES = "https://www.api.academiaaiapaec.edu.pe/api/sede/"

    // URL base para el servicio de Trbajador
    private const val BASE_URL_TRABAJADOR= "https://www.api.academiaaiapaec.edu.pe/api/trabajador/"

    // Instancia para el servicio de Asistencia
    val apiAsistencia: AsistenciaService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_ASISTENCIA)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AsistenciaService::class.java)
    }

    // Instancia para el servicio de Sedes
    val apiSedes: SedeService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_SEDES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SedeService::class.java)
    }
    val apiTrabajador: TrabajadorService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_TRABAJADOR)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrabajadorService::class.java)
    }


}