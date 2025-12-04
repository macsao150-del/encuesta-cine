package com.example.examencorte_3.repositories

import com.example.examencorte_3.repositories.SupabaseProvider.client
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.rpc

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
            filter {
                eq("id_opcion", id)
            }
        }
        val data = result.decodeList<Map<String, String>>()
        return data.firstOrNull()?.get("opcion")
    }


    // Obtener los titulos de las estadisticas
    suspend fun getEstadisticaTexto(id: Long): String? {
        val result = supabase.postgrest["Preguntas"].select(Columns.raw("textoEstadistica")) {
            filter {
                eq("id_pregunta", id)
            }
        }
        val data = result.decodeList<Map<String, String>>()
        return data.firstOrNull()?.get("textoEstadistica")
    }


    // Cantidad de veces que una opción fue seleccionada
    suspend fun loadConteo(id: Long): Int {
        val result = supabase.postgrest["Opciones"].select(Columns.raw("contador")) {
            filter {
                eq("id_opcion", id)
            }
        }
        val data = result.decodeList<Map<String, Int>>()
        return data.firstOrNull()?.get("contador") ?: 0
    }


    // Guardar una respuesta
    suspend fun insertarRespuesta(idPregunta: Long, idOpcion: Long, idEncuestado: Long): Boolean {
        return try {
            val data = mapOf(
                "id_pregunta" to idPregunta,
                "id_opcion" to idOpcion,
                "id_encuestado" to idEncuestado
            )
            supabase.postgrest["Respuestas"].insert(data)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getNumeroEncuestados(): Int {
        val result = supabase.postgrest.rpc("total_encuestados")
        return result.data.toString().toIntOrNull() ?:0



    }

}