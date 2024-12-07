package com.academiaaiapaec.appasistenciaaiapaec.model.response

import com.academiaaiapaec.appasistenciaaiapaec.model.Trabajador

data class TrabajadorResponse (
    val success: Boolean,
    val message: String,
    val data: Trabajador
)
