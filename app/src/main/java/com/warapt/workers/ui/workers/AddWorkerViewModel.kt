package com.warapt.workers.ui.workers

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warapt.workers.data.RetrofitClient
import com.warapt.workers.data.models.WorkerRequest
import kotlinx.coroutines.launch

class AddWorkerViewModel : ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _success = mutableStateOf(false)
    val success: State<Boolean> = _success

    fun addWorker(firstName: String, lastName: String, dni: String, age: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = RetrofitClient.api.addWorker(WorkerRequest(firstName, lastName, dni, age))
                if (response.isSuccessful) {
                    _success.value = true
                } else {
                    _errorMessage.value = "No se pudo agregar el trabajador"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}