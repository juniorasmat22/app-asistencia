package com.academiaaiapaec.appasistenciaaiapaec.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.academiaaiapaec.appasistenciaaiapaec.model.Estado
import com.academiaaiapaec.appasistenciaaiapaec.model.Turno
import com.academiaaiapaec.appasistenciaaiapaec.repository.EstadoRepository
import kotlinx.coroutines.launch

class EstadoViewModel: ViewModel() {
    private val repository= EstadoRepository()
    var isLoading = MutableLiveData<Boolean>()
    private val _estadoResult= MutableLiveData<Result<List<Estado>>>()
    val estadoResult: LiveData<Result<List<Estado>>> = _estadoResult

    fun getEstados(){
        viewModelScope.launch{
            isLoading.value = true
            var result = repository.getEstados()
            _estadoResult.value=result
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