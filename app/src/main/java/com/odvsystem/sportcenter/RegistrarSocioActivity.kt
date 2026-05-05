package com.odvsystem.sportcenter

import android.os.Bundle
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
        binding.encabezado.setTitulo("Nuevo Socio")
        binding.encabezado.setDestino(MenuActivity::class.java)

        


    }
}