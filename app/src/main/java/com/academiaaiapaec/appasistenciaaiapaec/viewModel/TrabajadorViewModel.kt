package com.academiaaiapaec.appasistenciaaiapaec.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.academiaaiapaec.appasistenciaaiapaec.model.response.TrabajadorResponse
import com.academiaaiapaec.appasistenciaaiapaec.model.response.response
import com.academiaaiapaec.appasistenciaaiapaec.repository.TrabajadorRepository
import kotlinx.coroutines.launch

class TrabajadorViewModel: ViewModel() {
    private val repository= TrabajadorRepository()
    var isLoading = MutableLiveData<Boolean>()
    private val _trabajadorResult= MutableLiveData<Result<TrabajadorResponse>>()
    val trabajadorResult: LiveData<Result<TrabajadorResponse>> = _trabajadorResult
    fun getTrabajador(idTrabajador:Int){
        viewModelScope.launch {
            isLoading.value = true
            var result=repository.obtenerTrabajador(idTrabajador)
            _trabajadorResult.value=result
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