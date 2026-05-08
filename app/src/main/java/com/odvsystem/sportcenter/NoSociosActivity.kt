package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.odvsystem.sportcenter.databinding.ActivityListaNoSociosBinding
import com.odvsystem.sportcenter.databinding.ActivityVencimientoBinding

class NoSociosActivity : AppCompatActivity() {
    public lateinit var binding: ActivityListaNoSociosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_no_socios)
        binding = ActivityListaNoSociosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

       binding.encabezado.setTitulo("NO SOCIO")
        binding.encabezado.setDestino(MenuActivity::class.java)

        val registrar: Button = findViewById<Button>(R.id.btnRegistrar)
        registrar.setOnClickListener {
            val intentar = Intent(this, RegistrarNoSocioActivity::class.java)
            startActivity(intentar)
        }


    }
}