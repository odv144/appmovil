package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        val socios: LinearLayout = findViewById<LinearLayout>(R.id.menu_Socio)
        val nosocio: LinearLayout = findViewById<LinearLayout>(R.id.menu_NoSocio)
        val vencimiento: LinearLayout=findViewById<LinearLayout>(R.id.menu_Vencimientos)
        socios.setOnClickListener {
            Toast.makeText(this,"presionando boton de socio", Toast.LENGTH_LONG).show()
            val intentar = Intent(this, SociosActivity::class.java)
            startActivity(intentar)
        }

        nosocio.setOnClickListener{
            val intentar = Intent(this, RegistrarNoSocioActivity::class.java)
            startActivity(intentar)
        }

        vencimiento.setOnClickListener {
            val intentar = Intent(this, VencimientoActivity::class.java)
            startActivity(intentar)
        }


    }
}