package com.odvsystem.sportcenter

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.database.DatabaseHelper

class ReimpresionCarnet : AppCompatActivity() {

    private var socioActual: Map<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reimpresion_carnet)

        // --- HEADER ---
        val tvTitulo: TextView = findViewById(R.id.tvTitulo)
        tvTitulo.text = "Reimpresión de Carnet"
        val btnAtras: Button = findViewById(R.id.btnAtras)
        btnAtras.setOnClickListener { finish() }

        // --- VISTAS DEL CARNET ---
        val tvNombre: TextView    = findViewById(R.id.tvNombreSocio)
        val tvDni: TextView       = findViewById(R.id.tvDniSocio)
        val tvActividad: TextView = findViewById(R.id.tvActividad)
        val tvEstado: TextView    = findViewById(R.id.tvEstado)
        val tvMembresia: TextView = findViewById(R.id.tvEstadoMembresia)

        // --- BÚSQUEDA ---
        val etBuscar: EditText = findViewById(R.id.etBuscarSocio)
        val btnBuscar: Button  = findViewById(R.id.btnBuscar)

        fun buscar() {
            val termino = etBuscar.text.toString().trim()
            if (termino.isEmpty()) {
                Toast.makeText(this, "Ingresá un número de socio o DNI", Toast.LENGTH_SHORT).show()
                return
            }

            val socio = DatabaseHelper(this).buscarSocioParaCarnet(termino)

            if (socio != null) {
                socioActual = socio

                tvNombre.text    = "${socio["apellido"]}, ${socio["nombre"]}"
                tvDni.text       = "DNI: ${socio["dni"]} · Socio Nº ${socio["nrosocio"]}"
                tvActividad.text = socio["actividades"]

                val esActivo = socio["estado"] == "activo"
                tvEstado.text    = if (esActivo) "Al día" else "Inhabilitado"
                tvMembresia.text = if (esActivo) "Activo y al día" else "Cuenta inhabilitada"
                tvMembresia.setTextColor(
                    android.graphics.Color.parseColor(if (esActivo) "#4CAF50" else "#F44336")
                )

            } else {
                socioActual      = null
                tvNombre.text    = "— Socio no encontrado —"
                tvDni.text       = ""
                tvActividad.text = "—"
                tvEstado.text    = "—"
                tvMembresia.text = "—"
                tvMembresia.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
                Toast.makeText(this, "No se encontró ningún socio", Toast.LENGTH_SHORT).show()
            }
        }

        btnBuscar.setOnClickListener { buscar() }

        // Busca también al presionar Enter en el teclado
        etBuscar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) { buscar(); true } else false
        }

        // --- IMPRIMIR ---
        val btnImprimir: Button = findViewById(R.id.btnImprimirCarnet)
        btnImprimir.setOnClickListener {
            val socio = socioActual
            if (socio == null) {
                Toast.makeText(this, "Primero buscá un socio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(
                this,
                "Enviando carnet de ${socio["apellido"]}, ${socio["nombre"]} a la impresora...",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}