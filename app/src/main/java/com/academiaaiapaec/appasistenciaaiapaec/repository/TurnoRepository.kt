package com.academiaaiapaec.appasistenciaaiapaec.repository

import com.academiaaiapaec.appasistenciaaiapaec.model.Turno

class TurnoRepository {
    fun getTurnos():Result<List<Turno>>{
        // Creando una lista de turnos manualmente
        val turnos = listOf(
            Turno(id_turno = 1, nombre_turno = "Mañana"),
            Turno(id_turno = 2, nombre_turno = "Tarde"),
            //Turno(id_turno = 3, nombre_turno = "Noche")
        )

        // Devolviendo el resultado envuelto en un Result.success
        return Result.success(turnos)
    }
}