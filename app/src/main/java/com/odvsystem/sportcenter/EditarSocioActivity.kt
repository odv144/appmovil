package com.odvsystem.sportcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.odvsystem.sportcenter.databinding.ActivityEditarSocioBinding

class EditarSocioActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityEditarSocioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarSocioBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_editar_socio)
        setContentView(binding.root)
        enableEdgeToEdge()
        val guardar: Button= findViewById<Button>(R.id.btnGuardar)
        val limpiar: Button= findViewById<Button>(R.id.btnLimpiar)
        binding.encabezado.setTitulo("Editar Socio")
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