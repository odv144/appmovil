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

        socios.setOnClickListener {
            Toast.makeText(this,"has precionado el layout", Toast.LENGTH_LONG).show()
            val intentar = Intent(this, MainActivity::class.java)
            startActivity(intentar)

        }

    }
}