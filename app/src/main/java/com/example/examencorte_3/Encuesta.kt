package com.example.examencorte_3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.examencorte_3.repositories.EncuestaRepository
import com.example.examencorte_3.repositories.SupabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Encuesta : AppCompatActivity() {
    private lateinit var txtFrecuencia: TextView
    private lateinit var txtCalidadAudio: TextView
    private lateinit var txtAtencion: TextView
    private lateinit var txtBaños: TextView
    private lateinit var txtTipoSala: TextView

    private fun initPreguntasMap() {
        preguntasMap = mapOf(
            txtFrecuencia to 1L,
            txtCalidadAudio to 2L,
            txtAtencion to 3L,
            txtBaños to 4L,
            txtTipoSala to 5L
        )
    }


    private lateinit var opcionesMap: MutableMap<RadioButton, Long>
    private lateinit var preguntasMap: Map<TextView, Long>


    private lateinit var btnFinalizar: Button
    private lateinit var progressBar: ProgressBar


    private val encuestaRepo = EncuestaRepository(SupabaseProvider.client)
//    private val storeViewModel: SharedViewModel by lazy {
//        AppViewModelStore.provider.get(SharedViewModel::class.java)
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_encuesta)
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
        btnFinalizar = findViewById(R.id.btnFinalizar)
        progressBar = findViewById(R.id.progressBar)

        initPreguntasMap()
        loadTodasLasPreguntas()

        initOpcionesMap()
        loadTodasLasOpciones()


        btnFinalizar.setOnClickListener {
            guardarRespuestas()
        }
    }

    private fun loadPregunta(textView: TextView, preguntaId: Long) {
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pregunta = encuestaRepo.getPregunta(preguntaId)
                runOnUiThread {
                    textView.text = pregunta ?: "Pregunta no encontrada"
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

    private fun loadTodasLasPreguntas() {
        preguntasMap.forEach { (textView, preguntaId) ->
            loadPregunta(textView, preguntaId)
        }
    }



    private fun getRadioButton(idName: String): RadioButton? {
        val id = resources.getIdentifier(idName, "id", packageName)
        return findViewById(id)
    }

    private fun loadOpciones(radioButton: RadioButton, opcionesId: Long) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val opciones = encuestaRepo.getOpcion(opcionesId)
                runOnUiThread {
                    radioButton.text = opciones ?: "Opción no encontrada"
                }
            } catch (e: Exception) {
                runOnUiThread {
                    radioButton.text = "Error: ${e.localizedMessage}"
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
                val idName = "btnRes${opcion}Pre${pregunta}"
                val rb = getRadioButton(idName)
                if (rb != null) {
                    opcionesMap[rb] = actualId++
                }
            }
        }
        for (opcion in 1..3) {
            val idName = "btnRes${opcion}Pre5"
            val rb = getRadioButton(idName)
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

    private fun guardarRespuestas() {
        progressBar.visibility = View.VISIBLE
        val idEncuestado = System.currentTimeMillis()
        CoroutineScope(Dispatchers.IO).launch {

            var todoCorrecto = true

            for ((textView, idPregunta) in preguntasMap) {
                val parent = textView.parent as View
                val radioGroupIdName = "rgPre$idPregunta"
                val radioGroupId = resources.getIdentifier(radioGroupIdName, "id", packageName)
                val radioGroup = parent.findViewById<RadioGroup>(radioGroupId)

                if (radioGroup == null) {
                    todoCorrecto = false
                    continue
                }

                val selectedId = radioGroup.checkedRadioButtonId

                if (selectedId == -1) {
                    todoCorrecto = false
                    continue
                }
                val radioButton = findViewById<RadioButton>(selectedId)
                val idOpcion = opcionesMap[radioButton]

                if (idOpcion != null) {
                    encuestaRepo.insertarRespuesta(idPregunta, idOpcion, idEncuestado)
                } else {
                    todoCorrecto = false
                }
            }
            runOnUiThread {
                progressBar.visibility = View.GONE

                if (todoCorrecto) {
                    Toast.makeText(this@Encuesta, "Respuestas enviadas correctamente", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Encuesta, MainActivity::class.java))
                } else {
                    Toast.makeText(this@Encuesta, "Faltan respuestas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}























