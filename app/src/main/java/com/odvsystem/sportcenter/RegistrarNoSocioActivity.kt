package com.odvsystem.sportcenter

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.odvsystem.sportcenter.databinding.ActivityMainBinding
import com.odvsystem.sportcenter.databinding.ActivityRegistrarNoSocioBinding
import com.odvsystem.sportcenter.databinding.ActivityRegistrarSocioBinding

class RegistrarNoSocioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarNoSocioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_no_socio)
        binding = ActivityRegistrarNoSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.encabezado.setTitulo("Registro No Socio")
        binding.encabezado.setDestino(SociosActivity::class.java)

    }
}