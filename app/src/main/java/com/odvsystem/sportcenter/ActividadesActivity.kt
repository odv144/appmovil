package com.odvsystem.sportcenter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.odvsystem.sportcenter.databinding.ActivityActividadesBinding
class ActividadesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActividadesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityActividadesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.encabezado.setTitulo("Actividades")
        binding.encabezado.setDestino(SociosActivity::class.java)
        }
    }