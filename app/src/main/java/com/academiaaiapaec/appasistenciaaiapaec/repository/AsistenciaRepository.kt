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
            Log.d("AsistenciaRegistro", "Iniciando solicitud para registrar asistencia con datos: $asistencia")

            // Realizar la solicitud a la API
            val response = api.registrarAsistencia(asistencia)
            Log.d("AsistenciaRegistro", "response: $response")
            // Verificar si la respuesta es exitosa
            if (response.isSuccessful && response.body() != null) {
                Log.d("AsistenciaRegistro", "Solicitud exitosa, respuesta recibida: ${response.body()}")
                // Devolver el resultado exitoso
                Result.success(response.body()!!)
            } else {
                // Si hay error en la respuesta, obtener el mensaje de error del cuerpo de la respuesta
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Log.e("AsistenciaRegistro", "Error en la respuesta: $errorBody")

                // Convertir el cuerpo de error en un objeto AsistenciaResponse para extraer el mensaje
                val errorResponse = Gson().fromJson(errorBody, AsistenciaResponse::class.java)
                var errorMessage = errorResponse.message  // Extraemos el 'message' del objeto

                // Mostrar el mensaje de error
                Log.e("AsistenciaRegistro", "Mensaje de error: $errorMessage")

                // Devolver el resultado con el error
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            // Capturar cualquier excepción y devolver el error
            Log.e("AsistenciaRegistro", "Excepción capturada: ${e.message}", e)
            Result.failure(e)
        }
    }
}
