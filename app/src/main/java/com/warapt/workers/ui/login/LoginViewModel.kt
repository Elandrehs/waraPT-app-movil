package com.warapt.workers.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warapt.workers.data.RetrofitClient
import com.warapt.workers.data.models.LoginRequest
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class LoginViewModel : ViewModel() {

    // "State" es una variable que, cuando cambia, hace que Compose
    // vuelva a dibujar automáticamente lo que dependa de ella
    // (equivalente a un @Input/estado reactivo de Angular)
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _loginSuccess = mutableStateOf(false)
    val loginSuccess: State<Boolean> = _loginSuccess

    fun login(username: String, password: String) {
        // viewModelScope.launch = corre esto en segundo plano (una corrutina),
        // así la llamada de red no congela la pantalla mientras espera al backend
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = RetrofitClient.api.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    _loginSuccess.value = true
                } else {
                    _errorMessage.value = "Usuario o contraseña incorrectos"
                }
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo conectar al servidor: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}