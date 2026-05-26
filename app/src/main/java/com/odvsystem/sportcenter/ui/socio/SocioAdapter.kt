package com.odvsystem.sportcenter.ui.socio

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.odvsystem.sportcenter.R
import com.odvsystem.sportcenter.model.Socio

class SocioAdapter (
    private var lista: MutableList<Socio>,
    private val onVer: (Socio) -> Unit,
    private val onEditar: (Socio) -> Unit,
    private val onEliminar: (Socio) -> Unit
) : RecyclerView.Adapter<SocioAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvDni: TextView = view.findViewById(R.id.tvDni)
        val tvActividad: TextView  = view.findViewById(R.id.tvActividad)
        val tvEstado: TextView  = view.findViewById(R.id.tvEstado)
        val btnDetalle: Button  = view.findViewById(R.id.btnDetalle)
        val btnEditar: Button   = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val vista =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_socio, parent, false)
        return ViewHolder(vista)
    }
    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        holder.tvNombre.text = "Socio:${item.nrosocio} nombre y apellido del join entre usuario y socio"
        holder.tvDni.text = item.nrosocio.toString()
        holder.tvActividad.text  = item.idusuario.toString()
        holder.tvEstado.text  = item.estadohabilitacion
        holder.btnDetalle.setOnClickListener  { onVer(item) }
        holder.btnEditar.setOnClickListener   { onEditar(item) }
        holder.btnEliminar.setOnClickListener { onEliminar(item) }
    }

    // Actualizar lista después de eliminar o editar
    @SuppressLint("NotifyDataSetChanged")
    fun actualizarLista(nuevaLista: MutableList<Socio>) {
        lista = nuevaLista
        // notifyDataSetChanged()
    }
}