package com.example.examencorte_3.repositories

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

class EncuestaRepository(private val supabase: SupabaseClient) {

    // Obtener una pregunta de una encuesta
    suspend fun getPregunta(id: Long): String? {
        val result = supabase.postgrest["Preguntas"].select(Columns.raw("pregunta")) {
            filter {
                eq("id_pregunta", id)
            }
        }
        val data = result.decodeList<Map<String, String>>()
        return data.firstOrNull()?.get("pregunta")
    }

    // Obtener las opciones de una pregunta
    suspend fun getOpcion(id: Long): String? {
        val result = supabase.postgrest["Opciones"].select(Columns.raw("opcion")) {
            filter{
                eq("id_opcion", id)
            }
        }
        val data = result.decodeList<Map<String, String>>()
        return data.firstOrNull()?.get("opcion")
    }

    // Guardar una respuesta (aun no esta listo ni en uso)
    suspend fun guardarRespuesta(id_pregunta: Long, id_opcion: Long) = supabase.postgrest["Respuestas"]
            .insert(
                mapOf(
                    "id_pregunta" to id_pregunta,
                    "id_opcion" to id_opcion
                )
            )
    }
