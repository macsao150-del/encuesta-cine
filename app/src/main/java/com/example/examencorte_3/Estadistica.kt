package com.example.examencorte_3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.examencorte_3.repositories.EncuestaRepository
import com.example.examencorte_3.repositories.SupabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.component1
import kotlin.collections.component2

class Estadistica : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var btnRegresar: Button
    private lateinit var txtNumEncuestados: TextView
    private lateinit var txtFrecuencia: TextView
    private lateinit var txtCalidadAudio: TextView
    private lateinit var txtAtencion: TextView
    private lateinit var txtBaños: TextView
    private lateinit var txtTipoSala: TextView

    private fun initTitulosMap() {
        titulosMap = mapOf(
            txtFrecuencia to 1L,
            txtCalidadAudio to 2L,
            txtAtencion to 3L,
            txtBaños to 4L,
            txtTipoSala to 5L
        )
    }


    private val encuestaRepo = EncuestaRepository(SupabaseProvider.client)
    private lateinit var titulosMap: Map<TextView, Long>

    private lateinit var conteoMap: MutableMap<TextView, Long>
    private lateinit var opcionesMap: MutableMap<TextView, Long>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_estadistica)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtFrecuencia = findViewById(R.id.txtFrecuencia)
        txtCalidadAudio = findViewById(R.id.txtCalidadAudio)
        txtAtencion = findViewById(R.id.txtAtencion)
        txtBaños = findViewById(R.id.txtBaños)
        txtTipoSala = findViewById(R.id.txtTipoSala)

        txtNumEncuestados = findViewById(R.id.txtNumEncuestados)
        progressBar = findViewById(R.id.progressBar)


        btnRegresar = findViewById(R.id.btnRegresar)

        btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        initTitulosMap()
        loadTodosLosTitulos()

        initconteoMap()
        loadTodosLosConteos()

        initOpcionesMap()
        loadTodasLasOpciones()

        loadNumeroEncuestados(txtNumEncuestados)

    }

    private fun loadTitulos(textView: TextView, estadisticaId: Long) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val titulo = encuestaRepo.getEstadisticaTexto(estadisticaId)
                runOnUiThread {
                    textView.text = titulo ?: "Titulo no encontrado"
                }
            } catch (e: Exception) {
                runOnUiThread {
                    textView.text = "Error: ${e.localizedMessage}"
                }
            } finally {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun loadTodosLosTitulos() {
        titulosMap.forEach { (textView, estadisticaId) ->
            loadTitulos(textView, estadisticaId)
        }
    }


    private fun loadNumeroEncuestados(textView: TextView) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val encuestados = "Número de personas encuestadas: " + encuestaRepo.getNumeroEncuestados()
                runOnUiThread {
                    textView.text = encuestados.toString()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    textView.text = "Error: ${e.localizedMessage}"
                }
            } finally {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun getTextView(idName: String): TextView? {
        val id = resources.getIdentifier(idName, "id", packageName)
        return findViewById(id)
    }

    private fun loadConteo(textView: TextView, opcionesId: Long) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val conteo = encuestaRepo.loadConteo(opcionesId)
                runOnUiThread {
                    textView.text = conteo.toString()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    textView.text = "Error: ${e.localizedMessage}"
                }
            } finally {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun initconteoMap() {
        conteoMap = mutableMapOf()
        var actualId = 1L

        for (pregunta in 1..4) {
            for (opcion in 1..4) {

                val idName = "txtRes${opcion}Pre${pregunta}"
                val rb = getTextView(idName)
                if (rb != null) {
                    conteoMap[rb] = actualId++
                }
            }
        }
        for (opcion in 1..3) {
            val idName = "txtRes${opcion}Pre5"
            val rb = getTextView(idName)
            if (rb != null) {
                conteoMap[rb] = actualId++
            }
        }
    }

    private fun loadTodosLosConteos() {
        conteoMap.forEach { (textView, opcionId) ->
            loadConteo(textView, opcionId)
        }
    }

    private fun loadOpciones(textView: TextView, opcionesId: Long) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val opciones = encuestaRepo.getOpcion(opcionesId)
                runOnUiThread {
                    textView.text = opciones ?: "Opción no encontrada"
                }
            } catch (e: Exception) {
                runOnUiThread {
                    textView.text = "Error: ${e.localizedMessage}"
                }
            } finally {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun initOpcionesMap() {
        opcionesMap = mutableMapOf()
        var actualId = 1L
        for (pregunta in 1..4) {
            for (opcion in 1..4) {
                val idName = "txtOp${opcion}Pre${pregunta}"
                val rb = getTextView(idName)
                if (rb != null) {
                    opcionesMap[rb] = actualId++
                }
            }
        }
        for (opcion in 1..3) {
            val idName = "txtOp${opcion}Pre5"
            val rb = getTextView(idName)
            if (rb != null) {
                opcionesMap[rb] = actualId++
            }
        }
    }

    private fun loadTodasLasOpciones() {
        opcionesMap.forEach { (radioButton, opcionId) ->
            loadOpciones(radioButton, opcionId)
        }
    }
}

