package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.databinding.ActivityRegistrarSocioBinding

class RegistrarSocioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarSocioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val guardar: Button= findViewById<Button>(R.id.btnGuardar)
        val limpiar: Button= findViewById<Button>(R.id.btnLimpiar)
        binding.encabezado.setTitulo("Nuevo Socio")
        binding.encabezado.setDestino(SociosActivity::class.java)

        guardar.setOnClickListener {
            val intentar = Intent(this, SociosActivity::class.java)
            startActivity(intentar)
        }

        limpiar.setOnClickListener {
            val intentar = Intent(this, SociosActivity::class.java)
            startActivity(intentar)
        }

        


    }
}