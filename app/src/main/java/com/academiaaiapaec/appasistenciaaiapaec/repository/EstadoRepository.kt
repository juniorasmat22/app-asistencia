package com.academiaaiapaec.appasistenciaaiapaec.repository

import com.academiaaiapaec.appasistenciaaiapaec.model.Estado
import com.academiaaiapaec.appasistenciaaiapaec.model.Turno

class EstadoRepository {
    fun getEstados():Result<List<Estado>>{
        // Creando una lista de turnos manualmente
        val estados = listOf(
            Estado(id_estado = 1, nombre_estado = "Entrada"),
            Estado(id_estado = 2, nombre_estado = "Salida"),
            //Turno(id_turno = 3, nombre_turno = "Noche")
        )

        // Devolviendo el resultado envuelto en un Result.success
        return Result.success(estados)
    }
}