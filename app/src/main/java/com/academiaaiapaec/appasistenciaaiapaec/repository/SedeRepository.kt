package com.academiaaiapaec.appasistenciaaiapaec.repository

import com.academiaaiapaec.appasistenciaaiapaec.model.response.response
import com.academiaaiapaec.appasistenciaaiapaec.singleton.RetrofitInstance

class SedeRepository {
    private val api = RetrofitInstance.apiSedes
    suspend fun obtenerSedes():Result<response>{
        return try {
            // Realizar la solicitud a la API
            val response = api.getSedes();

            // Verificar si la respuesta es exitosa
            if (response.isSuccessful && response.body() != null) {
                // Devolver el resultado exitoso
                Result.success(response.body()!!)
            } else {
                // Si la respuesta no fue exitosa, devolver un error
                Result.failure(Exception("Error en la respuesta de la API: ${response.message()}"))
            }
        } catch (e: Exception) {
            // Capturar cualquier excepción y devolver el error
            Result.failure(e)
        }
    }
}