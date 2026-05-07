package com.odvsystem.sportcenter

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ReimpresionCarnet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reimpresion_carnet)

        // --- CONFIGURACIÓN DEL HEADER ---
        val tvTitulo: TextView = findViewById(R.id.tvTitulo)
        tvTitulo.text = "Reimpresión de Carnet"

        val btnAtras: Button = findViewById(R.id.btnAtras)
        btnAtras.setOnClickListener {
            finish() // Vuelve a la pantalla anterior
        }

        // --- CONFIGURACIÓN DEL BOTÓN IMPRIMIR ---
        val btnImprimir: Button = findViewById(R.id.btnImprimirCarnet)

        btnImprimir.setOnClickListener {
            val nombreSocio = findViewById<TextView>(R.id.tvNombreSocio).text.toString()

            Toast.makeText(this, "Enviando carnet de $nombreSocio a la impresora...", Toast.LENGTH_LONG).show()

        }
    }
}