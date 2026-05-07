package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NoSociosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_no_socios)
        val registrar: Button = findViewById<Button>(R.id.btnRegistrar)
        registrar.setOnClickListener {
            val intentar = Intent(this, RegistrarNoSocioActivity::class.java)
            startActivity(intentar)
        }
    }
}