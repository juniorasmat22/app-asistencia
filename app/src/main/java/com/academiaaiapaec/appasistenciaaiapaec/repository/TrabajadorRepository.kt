package com.academiaaiapaec.appasistenciaaiapaec.repository

import android.util.Log
import com.academiaaiapaec.appasistenciaaiapaec.model.response.TrabajadorResponse
import com.academiaaiapaec.appasistenciaaiapaec.singleton.RetrofitInstance

class TrabajadorRepository {
    private val  api=RetrofitInstance.apiTrabajador
    suspend fun obtenerTrabajador(idTrabajador:Int):Result<TrabajadorResponse>{
        return try {
            // Realizar la solicitud a la API
            val response = api.getTrabajador(idTrabajador)
            // Verificar si la respuesta es exitosa
            if (response.isSuccessful && response.body() != null) {
                // Devolver el resultado exitoso

                Result.success(response.body()!!)
            } else {
                // Si la respuesta no fue exitosa, devolver un error y loguear el mensaje
                Log.e("API_ERROR", "Error en la respuesta de la API: ${response.message()}")
                // Si la respuesta no fue exitosa, devolver un error
                Result.failure(Exception("Error en la respuesta de la API: ${response.message()}"))
            }
        } catch (e: Exception) {
            // Capturar cualquier excepción y devolver el error
            Log.e("API_ERROR", "Excepción al obtener el trabajador: ${e.message}", e)
            Result.failure(e)
        }
    }
}