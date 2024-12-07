package com.academiaaiapaec.appasistenciaaiapaec.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.academiaaiapaec.appasistenciaaiapaec.model.response.response
import com.academiaaiapaec.appasistenciaaiapaec.repository.SedeRepository
import kotlinx.coroutines.launch


class SedeViewModel: ViewModel() {
    private val repository= SedeRepository()
    var isLoading = MutableLiveData<Boolean>()
    private val _sedesResult= MutableLiveData<Result<response>>()
    val sedeResult: LiveData<Result<response>> = _sedesResult
    fun getSede(){
        viewModelScope.launch{
            isLoading.value = true
            var result = repository.obtenerSedes()
            _sedesResult.value=result
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