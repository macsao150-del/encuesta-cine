package com.example.examencorte_3.repositories

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseProvider {
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://owvqwnxktjyaufnhhlyk.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im93dnF3bnhrdGp5YXVmbmhobHlrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjI1MTgwMTAsImV4cCI6MjA3ODA5NDAxMH0.-BJ858jJWgSN3JXfSoitp9W7g-AS-tCePNUX1w4YOu4"
        ) {
            install(Postgrest)
        }
    }

}