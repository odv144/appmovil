package com.odvsystem.sportcenter.ui.actividad

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.odvsystem.sportcenter.MenuActivity
import com.odvsystem.sportcenter.R
import com.odvsystem.sportcenter.NuevaActividad
import com.odvsystem.sportcenter.databinding.ActivityActividadesBinding
import com.odvsystem.sportcenter.model.Actividad
import com.odvsystem.sportcenter.repository.ActividadRepository

class ActividadActivity: AppCompatActivity() {
    private lateinit var repo: ActividadRepository
    private lateinit var adapter: ActividadAdapter
    private lateinit var binding: ActivityActividadesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades)
        binding = ActivityActividadesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.encabezado.setTitulo("Actividades")
        binding.encabezado.setDestino(MenuActivity::class.java)

        repo = ActividadRepository(this)
        val recycler = findViewById<RecyclerView>(R.id.recyclerActividades)

        adapter = ActividadAdapter(
            lista      = repo.obtenerTodos().toMutableList(),
            onVer      = { actividad -> mostrarDetalle(actividad) },
            onEditar   = { actividad -> irAEditar(actividad) },
            onEliminar = { actividad -> confirmarEliminar(actividad) }
        )
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        findViewById<Button>(R.id.btnNuevaActividad).setOnClickListener {
            irAEditar(null) // null = nueva actividad
        }


    }

    // Recargar lista al volver de editar
    override fun onResume() {
        super.onResume()
        adapter.actualizarLista(repo.obtenerTodos().toMutableList())
    }

    private fun mostrarDetalle(actividad: Actividad) {
        AlertDialog.Builder(this)
            .setTitle(actividad.nombre)
            .setMessage("""
                📋 ${actividad.descripcion}
                
                🕐 Turno: ${actividad.turno}
                👥 Cupo máximo: ${actividad.cupoMaximo}
                💰 Tarifa socio: $${actividad.tarifaSocio}
                💰 Tarifa no socio: $${actividad.tarifaNoSocio}
            """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun irAEditar(actividad: Actividad?) {
        Toast.makeText(this, "Intentando editar", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, NuevaActividad::class.java)
        actividad?.let { intent.putExtra("idActividad", it.idActividad) }
        startActivity(intent)
    }

    private fun confirmarEliminar(actividad: Actividad) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar actividad")
            .setMessage("¿Seguro que querés eliminar ${actividad.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                val ok = repo.eliminar(actividad.idActividad)
                if (ok) {
                    adapter.actualizarLista(repo.obtenerTodos().toMutableList())
                    Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

}