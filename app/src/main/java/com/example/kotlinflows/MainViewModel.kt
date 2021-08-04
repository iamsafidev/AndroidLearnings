package com.example.kotlinflows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    fun loginUser(name: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            delay(2000L)
            if (name == "android" && password == "topsecret") {
                _loginUiState.value = LoginUiState.Success
            } else {
                _loginUiState.value = LoginUiState.Error("Invalid Credentials. Try Again")
            }
        }
    }

    val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    var loginUiState: StateFlow<LoginUiState> = _loginUiState


    sealed class LoginUiState {
        object Success : LoginUiState()
        object Loading : LoginUiState()
        object Empty : LoginUiState()
        data class Error(val message: String) : LoginUiState()
    }
}