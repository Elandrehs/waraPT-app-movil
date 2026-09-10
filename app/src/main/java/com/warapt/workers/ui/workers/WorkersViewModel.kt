package com.warapt.workers.ui.workers

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warapt.workers.data.RetrofitClient
import com.warapt.workers.data.models.Worker
import kotlinx.coroutines.launch

class WorkersViewModel : ViewModel() {

    private val _workers = mutableStateOf<List<Worker>>(emptyList())
    val workers: State<List<Worker>> = _workers

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Se llama al entrar a la pantalla, y cada vez que cambia el filtro
    fun loadWorkers(dni: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = RetrofitClient.api.getWorkers(dni?.ifBlank { null })
                if (response.isSuccessful) {
                    _workers.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Error al cargar trabajadores"
                }
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo conectar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}