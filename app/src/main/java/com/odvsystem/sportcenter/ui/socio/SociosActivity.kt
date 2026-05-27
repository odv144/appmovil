package com.odvsystem.sportcenter.ui.socio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.odvsystem.sportcenter.MenuActivity
import com.odvsystem.sportcenter.NuevaActividad
import com.odvsystem.sportcenter.R
import com.odvsystem.sportcenter.RegistrarSocioActivity
import com.odvsystem.sportcenter.databinding.ActivitySociosBinding
import com.odvsystem.sportcenter.model.Actividad
import com.odvsystem.sportcenter.model.Socio
import com.odvsystem.sportcenter.repository.SocioRepository

class SociosActivity : AppCompatActivity() {

    val listaSocios = mutableListOf<Socio>()
    private lateinit var repo: SocioRepository
    private lateinit var adapter: SocioAdapter
    private lateinit var binding: ActivitySociosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySociosBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_socios)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.encabezado.setTitulo("SOCIOS")
        binding.encabezado.setDestino(MenuActivity::class.java)

        repo = SocioRepository(this)
        val rvSocio = findViewById<RecyclerView>(R.id.rvSocios)

        adapter = SocioAdapter(
            lista      = repo.obtenerTodos().toMutableList(),
            onVer      = { socio -> mostrarDetalle(socio) },
            onEditar   = { socio -> irAEditar(socio) },
            onEliminar = { socio -> confirmarEliminar(socio) }
        )
        findViewById<Button>(R.id.btnRegistrar).setOnClickListener {
            irAEditar(null) // null = nueva actividad
        }

/*
        registrar.setOnClickListener {
            val intentar = Intent(this, RegistrarSocioActivity::class.java).apply {

            }
            startActivity(intentar)

        }
  */}
        // Recargar lista al volver de editar
        override fun onResume() {
            super.onResume()
            adapter.actualizarLista(repo.obtenerTodos().toMutableList())
        }

        private fun mostrarDetalle(socio: Socio) {
            AlertDialog.Builder(this)
                .setTitle(socio.nrosocio)
                .setMessage("""
                📋 ${socio.idusuario}
                
                🕐 Estado: ${socio.estadohabilitacion}
                👥 Cuota: ${socio.cuotamensual}
                💰 Presento carnet: $${socio.carneteentregado}             
            """.trimIndent())
                .setPositiveButton("Cerrar", null)
                .show()
        }

        private fun irAEditar(socio: Socio?) {
            Toast.makeText(this, "Intentando editar", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RegistrarSocioActivity::class.java)
            socio?.let { intent.putExtra("nroSocio", it.nrosocio) }
            startActivity(intent)
        }

        private fun confirmarEliminar(socio: Socio) {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Socio")
                .setMessage("¿Seguro que querés eliminar ${socio.nrosocio}?")
                .setPositiveButton("Eliminar") { _, _ ->
                    val ok = repo.eliminar(socio.nrosocio)
                    if (ok) {
                        adapter.actualizarLista(repo.obtenerTodos().toMutableList())
                        Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

    }
