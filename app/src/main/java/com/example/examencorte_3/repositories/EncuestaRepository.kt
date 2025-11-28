package com.example.examencorte_3.repositories

import com.example.examencorte_3.repositories.SupabaseProvider.client
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
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

    // Guardar una respuesta (metodo listo, falta implementarlo en la vista de encuesta)
    suspend fun insertarRespuesta(idPregunta: Long, idOpcion: Long): Boolean {
        return try {
            val data = mapOf(
                "id_pregunta" to idPregunta,
                "id_opcion" to idOpcion
            )
            supabase.postgrest["Respuestas"].insert(data)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
