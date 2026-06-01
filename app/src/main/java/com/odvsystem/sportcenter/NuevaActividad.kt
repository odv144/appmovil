package com.odvsystem.sportcenter

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class NuevaActividad : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nueva_actividad)


        val tvTitulo: TextView = findViewById(R.id.tvTitulo)
        val eflag = intent.getBooleanExtra("FLAG", false)
        if (eflag) {

        }
        else{
            tvTitulo.text = "Editar Actividad"
        }
        val btnAtras: Button = findViewById(R.id.btnAtras)
        btnAtras.setOnClickListener {

        }

        // Botón GUARDAR
        val btnGuardar: Button = findViewById(R.id.btnGuardar)
        btnGuardar.setOnClickListener {
            finish()
        }

        // Botón LIMPIAR
        val btnLimpiar: Button = findViewById(R.id.btnLimpiar)
        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    // Función extra para que el código sea más ordenado
    private fun limpiarCampos() {
        findViewById<EditText>(R.id.etNombreActividad).text.clear()
        findViewById<EditText>(R.id.etDescripcion).text.clear()
        findViewById<EditText>(R.id.etTurno).text.clear()
        findViewById<EditText>(R.id.etCupo).text.clear()
        findViewById<EditText>(R.id.etTarifaDiaria).text.clear()
        findViewById<EditText>(R.id.etTarifaMensual).text.clear()
    }
}