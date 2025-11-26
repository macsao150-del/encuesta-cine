package com.example.examencorte_3.models

import kotlinx.serialization.Serializable

@Serializable
data class Preguntas (
    val id_pregunta: Long? = null,
    val created_at: String? = null,
    val pregunta: String,
    val id_encuesta: Long
)