package com.example.examencorte_3.repositories

import com.example.examencorte_3.models.Opciones
import com.example.examencorte_3.models.Preguntas
import com.example.examencorte_3.models.Respuestas
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class EncuestaRepository(private val supabase: SupabaseClient) {


    // Obtener las preguntas de una encuesta
    suspend fun getPreguntas() = supabase.postgrest["Preguntas"]
        .select {
            filter {
                eq("id_encuesta", 1)
            }
        }
        .decodeList<Preguntas>()


    // Obtener las opciones de una pregunta
    suspend fun getOpciones(id_pregunta: Long) = supabase.postgrest["Opciones"]
        .select {
            filter{
                eq("id_pregunta", id_pregunta)
            }
        }
        .decodeList<Opciones>()


    // Guardar una respuesta
    suspend fun guardarRespuesta(id_pregunta: Long, id_opcion: Long) = supabase.postgrest["Respuestas"]
            .insert(
                mapOf(
                    "id_pregunta" to id_pregunta,
                    "id_opcion" to id_opcion
                )
            )
    }
