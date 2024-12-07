package com.academiaaiapaec.appasistenciaaiapaec.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.academiaaiapaec.appasistenciaaiapaec.model.Turno
import com.academiaaiapaec.appasistenciaaiapaec.repository.TurnoRepository
import kotlinx.coroutines.launch

class TurnoViewModel: ViewModel() {
    private val repository= TurnoRepository()
    var isLoading = MutableLiveData<Boolean>()
    private val _turnoResult= MutableLiveData<Result<List<Turno>>>()
    val turnoResult: LiveData<Result<List<Turno>>> = _turnoResult

    fun getTurnos(){
        viewModelScope.launch{
            isLoading.value = true
            var result = repository.getTurnos()
            _turnoResult.value=result
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