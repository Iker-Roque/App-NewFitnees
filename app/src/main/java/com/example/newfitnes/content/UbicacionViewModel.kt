package com.example.newfitnes.content

import androidx.lifecycle.ViewModel
import com.example.newfitnes.api.ApiServices
import com.example.newfitnes.api.Ubicacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UbicacionViewModel(private val api: ApiServices) : ViewModel() {

    private val _ubicaciones = MutableStateFlow<List<Ubicacion>>(emptyList())
    val ubicaciones: StateFlow<List<Ubicacion>> get() = _ubicaciones

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    // AGREGAR EL ESTADO DE LOADING
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun loadUbicaciones() {
        // Activar el loading al iniciar la petición
        _isLoading.value = true
        _error.value = null // Limpiar errores anteriores

        api.getUbicaciones().enqueue(object : Callback<List<Ubicacion>> {
            override fun onResponse(call: Call<List<Ubicacion>>, response: Response<List<Ubicacion>>) {
                // Desactivar loading cuando termine la petición
                _isLoading.value = false

                if (response.isSuccessful) {
                    _ubicaciones.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error del servidor: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<List<Ubicacion>>, t: Throwable) {
                // Desactivar loading en caso de error
                _isLoading.value = false
                _error.value = "Error de conexión: ${t.message}"
            }
        })
    }

    // Función adicional para retry
    fun retry() {
        loadUbicaciones()
    }

    // Función para limpiar errores manualmente
    fun clearError() {
        _error.value = null
    }
}