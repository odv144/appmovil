package com.odvsystem.sportcenter.ui.nosocio

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.odvsystem.sportcenter.R
import com.odvsystem.sportcenter.model.NoSocioRegistro

class NoSocioAdapter(
    private var lista: MutableList<NoSocioRegistro>,
    private val onVer: (NoSocioRegistro) -> Unit,
    private val onEditar: (NoSocioRegistro) -> Unit,
    private val onEliminar: (NoSocioRegistro) -> Unit
) : RecyclerView.Adapter<NoSocioAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvDni: TextView = view.findViewById(R.id.tvDni)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val btnDetalle: Button = view.findViewById(R.id.btnDetalle)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.tvNombre.text = "No Socio: ${item.apellido}, ${item.nombre}"
        holder.tvDni.text = "DNI: ${item.dni}"
        holder.tvEstado.text = "Obs: ${item.observacion}"

        holder.btnDetalle.setOnClickListener { onVer(item) }
        holder.btnEditar.setOnClickListener { onEditar(item) }
        holder.btnEliminar.setOnClickListener { onEliminar(item) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun actualizarLista(nuevaLista: MutableList<NoSocioRegistro>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}