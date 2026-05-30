package com.odvsystem.sportcenter.ui.socio

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.odvsystem.sportcenter.MenuActivity
import com.odvsystem.sportcenter.R
import com.odvsystem.sportcenter.RegistrarSocioActivity
import com.odvsystem.sportcenter.databinding.ActivitySociosBinding
import com.odvsystem.sportcenter.model.SocioRegistro
import com.odvsystem.sportcenter.repository.SocioRepository

class SociosActivity : AppCompatActivity() {

    val listaSocios = mutableListOf<SocioRegistro>()
    private lateinit var repo: SocioRepository
    private lateinit var adapter: SocioAdapter
    private lateinit var binding: ActivitySociosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySociosBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // enableEdgeToEdge()
        binding.encabezado.setTitulo("SOCIOS")
        binding.encabezado.setDestino(MenuActivity::class.java)

        repo = SocioRepository(this)
        val rvSocio = findViewById<RecyclerView>(R.id.rvSocios)

        adapter = SocioAdapter(
            lista = repo.obtenerTodos().toMutableList(),
            onVer = { usuarioSocio -> mostrarDetalle(usuarioSocio) },
            onEditar = { usuarioSocio -> irAEditar(usuarioSocio) },
            onEliminar = { usuarioSocio -> confirmarEliminar(usuarioSocio) }
        )
        rvSocio.layoutManager = LinearLayoutManager(this)
        rvSocio.adapter = adapter

        findViewById<Button>(R.id.btnRegistrar).setOnClickListener {
            irAEditar(null) // null = nueva actividad
        }

        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtro = s.toString()
                adapter.actualizarLista(repo.obtenerSocioFiltro(filtro).toMutableList())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
        // Recargar lista al volver de editar
        override fun onResume() {
            super.onResume()
            adapter.actualizarLista(repo.obtenerTodos().toMutableList())

        }

        private fun mostrarDetalle(socio: SocioRegistro) {
            AlertDialog.Builder(this)
                .setTitle("📋 Socio Nro: ${socio.nrosocio.toString()}")
                .setMessage("""
                📋 ${socio.apellido}, ${socio.nombre}
                🕐 Estado: ${socio.estadohabilitacion.toString()}
                👥 Cuota: $${socio.cuotamensual.toString()}
                💰 DNI: ${socio.dni.toString()}             
            """.trimIndent())
                .setPositiveButton("Cerrar", null)
                .show()
        }

        private fun irAEditar(socio: SocioRegistro?) {
            Toast.makeText(this, "Intentando editar", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RegistrarSocioActivity::class.java)
            socio?.let { intent.putExtra("nroSocio", it.nrosocio) }
            startActivity(intent)
        }

        private fun confirmarEliminar(socio: SocioRegistro) {
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
