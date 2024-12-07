package com.academiaaiapaec.appasistenciaaiapaec.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.academiaaiapaec.appasistenciaaiapaec.model.Asistencia
import com.academiaaiapaec.appasistenciaaiapaec.model.response.AsistenciaResponse
import com.academiaaiapaec.appasistenciaaiapaec.repository.AsistenciaRepository
import kotlinx.coroutines.launch

class AsistenciaViewModel: ViewModel() {
    private val repository = AsistenciaRepository()
    var isLoading = MutableLiveData<Boolean>()
    // LiveData para el resultado de la operación
    private val _asistenciaResult = MutableLiveData<Result<AsistenciaResponse>>()
    val asistenciaResult: LiveData<Result<AsistenciaResponse>> = _asistenciaResult


    // Establecer isLoading a true antes de iniciar la operación
    fun registrarAsistencia(asistencia: Asistencia) {
        viewModelScope.launch {
            // Establecer isLoading a true antes de iniciar la operación
            isLoading.value = true
            // Realizar la operación de registrar asistencia en el repositorio
            val result = repository.registrarAsistencia(asistencia)
            // Actualizar el LiveData con el resultado
            _asistenciaResult.value = result

            // Cambiar el estado de carga a false una vez terminada la operación
            processFinished()
        }
    }
   private fun processFinished(){
        viewModelScope.launch {
            isLoading.value = false

        }
    }
}