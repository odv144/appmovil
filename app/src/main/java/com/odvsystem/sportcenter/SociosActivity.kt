package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.odvsystem.sportcenter.databinding.ActivityActividadesBinding
import com.odvsystem.sportcenter.databinding.ActivityRegistrarSocioBinding
import com.odvsystem.sportcenter.databinding.ActivitySociosBinding

class SociosActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySociosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySociosBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_socios)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.encabezado.setTitulo("SOCIOS")
        binding.encabezado.setDestino(MenuActivity::class.java)

        val registrar: Button = findViewById<Button>(R.id.btnRegistrar)
        val editar: Button= findViewById<Button>(R.id.btnEditar)

        registrar.setOnClickListener {

            val intentar = Intent(this, RegistrarSocioActivity::class.java).apply {

            }
            startActivity(intentar)
        }
        editar.setOnClickListener {
            val intentar = Intent(this, EditarSocioActivity::class.java).apply {
            }
            startActivity(intentar)
        }

    }
}