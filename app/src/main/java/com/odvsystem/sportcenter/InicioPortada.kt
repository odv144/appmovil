package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class InicioPortada : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_portada)

        val boton: Button = findViewById(R.id.btnIniciarSesion)

        boton.setOnClickListener {
            val intentar = Intent(this, MainActivity::class.java)
            startActivity(intentar)
        }
    }
}