package com.odvsystem.sportcenter.ui.nosocio

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.odvsystem.sportcenter.MenuActivity
import com.odvsystem.sportcenter.databinding.ActivityNoSociosBinding
import com.odvsystem.sportcenter.repository.NoSocioRepository

class NoSociosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoSociosBinding
    private lateinit var repository: NoSocioRepository
    private lateinit var adapter: NoSocioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoSociosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = NoSocioRepository(this)

        binding.encabezado.setTitulo("NO SOCIO")
        binding.encabezado.setDestino(MenuActivity::class.java)

        adapter = NoSocioAdapter(
            mutableListOf(),
            onVer = { noSocio ->
                AlertDialog.Builder(this)
                    .setTitle("📋 No Socio Nro: ${noSocio.nronosocio}")
                    .setMessage("""
            👤 ${noSocio.nombre}, ${noSocio.apellido}
            🆔 DNI: ${noSocio.dni}
            📞 Teléfono: ${noSocio.telefono}
            ✉️ Correo: ${noSocio.correo}
            📝 Observación: ${noSocio.observacion}
        """.trimIndent())
                    .setPositiveButton("Cerrar", null)
                    .show()
            },
            onEditar = { noSocio ->
                val intent = Intent(this, EditarNoSocioActivity::class.java)
                intent.putExtra("nronosocio", noSocio.nronosocio)
                startActivity(intent)
            },
            onEliminar = { noSocio ->   AlertDialog.Builder(this)
                .setTitle("Eliminar no socio")
                .setMessage("¿Estás seguro de eliminar a ${noSocio.nombre} ${noSocio.apellido}?")
                .setPositiveButton("Sí, eliminar") { _, _ ->

                    val eliminado = repository.eliminar(noSocio.nronosocio)

                    if (eliminado) {
                        Toast.makeText(this, "No socio eliminado", Toast.LENGTH_SHORT).show()
                        cargarNoSocios()
                    } else {
                        Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
            }
        )

        binding.rvNoSocios.layoutManager = LinearLayoutManager(this)
        binding.rvNoSocios.adapter = adapter

        binding.btnRegistrarNoSocio.setOnClickListener {
            startActivity(Intent(this, RegistrarNoSocioActivity::class.java))
        }

        cargarNoSocios()
    }

    override fun onResume() {
        super.onResume()
        cargarNoSocios()
    }

    private fun cargarNoSocios() {
        val lista = repository.obtenerTodos().toMutableList()
        adapter.actualizarLista(lista)
    }
}