package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.databinding.ActivityActividadesBinding
class ActividadesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActividadesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades)
        binding = ActivityActividadesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val newActi : Button =findViewById<Button>(R.id.btnNuevaActividad)
        val edit : Button =findViewById<Button>(R.id.btnEditar)

        binding.encabezado.setTitulo("Actividades")
        binding.encabezado.setDestino(MenuActivity::class.java)

        newActi.setOnClickListener {
           val vistaNuevaActividad = Intent(this, NuevaActividad::class.java)
            startActivity(vistaNuevaActividad)
        }
        edit.setOnClickListener {
            val vistaNuevaActividad = Intent(this, NuevaActividad::class.java)
            startActivity(vistaNuevaActividad)
        }

    }


    }