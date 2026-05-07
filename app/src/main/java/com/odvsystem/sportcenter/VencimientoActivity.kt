package com.odvsystem.sportcenter

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.databinding.ActivityVencimientoBinding

class VencimientoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVencimientoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVencimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.encabezado.setTitulo("Vencimientos")
        binding.encabezado.setDestino(SociosActivity::class.java)
    }
}