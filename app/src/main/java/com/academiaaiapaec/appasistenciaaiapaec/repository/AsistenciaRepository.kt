package com.academiaaiapaec.appasistenciaaiapaec.repository

import android.util.Log
import com.academiaaiapaec.appasistenciaaiapaec.model.Asistencia
import com.academiaaiapaec.appasistenciaaiapaec.model.response.AsistenciaResponse
import com.academiaaiapaec.appasistenciaaiapaec.singleton.RetrofitInstance
import com.google.gson.Gson
import retrofit2.Response

class AsistenciaRepository {
    private val api = RetrofitInstance.apiAsistencia

    // Función para registrar asistencia con manejo de excepciones
    suspend fun registrarAsistencia(asistencia: Asistencia): Result<AsistenciaResponse> {
        return try {
            // Realizar la solicitud a la API
            val response = api.registrarAsistencia(asistencia)
            Log.d("API_ERROR",response.toString())
            // Verificar si la respuesta es exitosa
            if (response.isSuccessful && response.body() != null) {
                // Devolver el resultado exitoso
                Result.success(response.body()!!)
            } else {
                // Si hay error en la respuesta, obtener el mensaje de error del cuerpo de la respuesta
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                // Convertir el cuerpo de error en un objeto AsistenciaResponse para extraer el mensaje
                val errorResponse = Gson().fromJson(errorBody, AsistenciaResponse::class.java)
                var errorMessage = errorResponse.message  // Extraemos el 'message' del objeto
                Log.e("API_ERROR", "Error: $errorMessage")

                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            // Capturar cualquier excepción y devolver el error
            Result.failure(e)
        }
    }
}
