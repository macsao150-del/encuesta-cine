package com.example.examencorte_3.models

import kotlinx.serialization.Serializable

@Serializable
data class Encuestas (
    val id_encuesta: Long? = null,
    val created_at: String? = null,
    val encuesta: String
)