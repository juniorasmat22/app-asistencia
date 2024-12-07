package com.academiaaiapaec.appasistenciaaiapaec.model.response

data class AsistenciaResponse(
    val success: Boolean,
    val message: String,
    val data: Any?,
    val error: String? = null // Esto puede ser nulo en caso de éxito
)