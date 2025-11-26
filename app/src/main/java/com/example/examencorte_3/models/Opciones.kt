package com.example.examencorte_3.models

import kotlinx.serialization.Serializable

@Serializable
data class Opciones (
    val id_opcion: Long? = null,
    val created_at: String? = null,
    val id_pregunta: Long,
    val opcion: String
)