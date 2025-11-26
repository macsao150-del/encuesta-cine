package com.example.examencorte_3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.examencorte_3.models.Respuestas

class SharedViewModel : ViewModel() {

    private val _respuestasContestadas = MutableLiveData<MutableList<Respuestas>>(mutableListOf())
    val respuestasContestadas: LiveData<MutableList<Respuestas>> get() = _respuestasContestadas

    fun guardarRespuesta(respuesta: Respuestas) {
        val lista = _respuestasContestadas.value ?: mutableListOf()

        val existente= lista.indexOfFirst { it.id_pregunta == respuesta.id_pregunta }

        if (existente != -1) {
            lista[existente] = respuesta  // actualizar
        } else {
            lista.add(respuesta)
        }
            _respuestasContestadas.value = lista
    }

    fun limpiarRespuestas() {
        _respuestasContestadas.value = mutableListOf()
    }
}

