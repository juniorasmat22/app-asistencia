package com.academiaaiapaec.appasistenciaaiapaec.model.response

import com.academiaaiapaec.appasistenciaaiapaec.model.Sede

data class response(
    val success: Boolean,
    val message: String,
    val data: DataResponse
)

data class DataResponse(
    val data: List<Sede>,
    val totalData: Double
)