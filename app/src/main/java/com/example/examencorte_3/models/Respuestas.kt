package com.example.examencorte_3.models

import kotlinx.serialization.Serializable

@Serializable
data class Respuestas (
    val id_respuesta: Long? = null,
    val created_at: String? = null,
    val id_pregunta: Long,
    val id_opcion: Long
)