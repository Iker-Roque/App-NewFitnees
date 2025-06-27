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

    fun loadUbicaciones() {
        api.getUbicaciones().enqueue(object : Callback<List<Ubicacion>> {
            override fun onResponse(call: Call<List<Ubicacion>>, response: Response<List<Ubicacion>>) {
                if (response.isSuccessful) {
                    _ubicaciones.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            }
            override fun onFailure(call: Call<List<Ubicacion>>, t: Throwable) {
                _error.value = "Error: ${t.message}"
            }
        })
    }
}
