package com.odvsystem.sportcenter.ui.actividad

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.odvsystem.sportcenter.R
import com.odvsystem.sportcenter.model.Actividad

class ActividadAdapter (
    private var lista: MutableList<Actividad>,
    private val onVer: (Actividad) -> Unit,
    private val onEditar: (Actividad) -> Unit,
    private val onEliminar: (Actividad) -> Unit
    ) : RecyclerView.Adapter<ActividadAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNombre: TextView = view.findViewById(R.id.tvNombre)
            val tvTurno: TextView = view.findViewById(R.id.tvTurno)
            val tvDescripcion: TextView  = view.findViewById(R.id.tvDescripcion)
            val tvTarifaSocio: TextView  = view.findViewById(R.id.tvTarifaSocio)
            val tvTarifaNoSocio: TextView= view.findViewById(R.id.tvTarifaNoSocio)
            val tvCupo: TextView         = view.findViewById(R.id.tvCupo)
            val btnDetalle: Button  = view.findViewById(R.id.btnDetalle)
            val btnEditar: Button   = view.findViewById(R.id.btnEditar)
            val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_actividad, parent, false)
            )

        override fun getItemCount() = lista.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = lista[position]

            holder.tvNombre.text = item.nombre
            holder.tvTurno.text = item.turno
            holder.tvDescripcion.text  = item.descripcion
            holder.tvTarifaSocio.text  = "Socio: $${item.tarifaSocio}"
            holder.tvTarifaNoSocio.text= "No socio: $${item.tarifaNoSocio}"
            holder.tvCupo.text = "Cupo: ${item.cupoMaximo}"

            holder.btnDetalle.setOnClickListener  { onVer(item) }
            holder.btnEditar.setOnClickListener   { onEditar(item) }
            holder.btnEliminar.setOnClickListener { onEliminar(item) }
        }

        // Actualizar lista después de eliminar o editar
        @SuppressLint("NotifyDataSetChanged")
        fun actualizarLista(nuevaLista: MutableList<Actividad>) {
            lista = nuevaLista
           // notifyDataSetChanged()
        }
}