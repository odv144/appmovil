package com.odvsystem.sportcenter

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ListadoCarnet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listado_carnet)

        // --- 1. CONFIGURACIÓN DEL HEADER ---
        val tvTitulo: TextView = findViewById(R.id.tvTitulo)
        tvTitulo.text = "Listado de Socios"

        val btnAtras: Button = findViewById(R.id.btnAtras)
        btnAtras.setOnClickListener {
            finish() // Vuelve a la pantalla principal
        }

        // --- 2. LÓGICA DEL BUSCADOR ---
        val etBuscar: EditText = findViewById(R.id.etBuscarSocio)
        val btnBuscar: Button = findViewById(R.id.btnBuscar)

        btnBuscar.setOnClickListener {
            val nombreABuscar = etBuscar.text.toString()

            if (nombreABuscar.isNotEmpty()) {

                // Mensaje (Toast) con lo ingresado.
                Toast.makeText(this, "Buscando a: $nombreABuscar", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, ingresa un nombre o DNI", Toast.LENGTH_SHORT).show()
            }
        }
    }
}