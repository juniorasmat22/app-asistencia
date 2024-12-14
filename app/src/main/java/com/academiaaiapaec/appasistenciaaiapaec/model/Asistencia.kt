package com.academiaaiapaec.appasistenciaaiapaec.model

data class Asistencia(
    val id_trabajador: Int,
    val fecha: String,
    val hora: String,
    val id_turno: Int,
    val id_sede: Int,
    val tipo: String,
    val observacion: String?,
    val ip: String,
    val latitud: String,
    val longitud: String
)
