package com.odvsystem.sportcenter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VencimientoAdapter (
    private val lista: List<Vencimiento>,
    private val onClick: (Vencimiento) -> Unit
    ) : RecyclerView.Adapter<VencimientoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvPeriodo: TextView = view.findViewById(R.id.tvPeriodo)
        val tvMonto: TextView = view.findViewById(R.id.tvMonto)
        val tvVence: TextView = view.findViewById(R.id.tvVence)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_vencimiento, parent, false)
        )

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.tvNombre.text = item.nombre
        holder.tvPeriodo.text = item.periodo
        holder.tvMonto.text = item.monto
        holder.tvVence.text = item.vence

        when (item.estado) {
            "al_dia" -> {
                holder.tvEstado.text = "✓"
                holder.tvEstado.setTextColor(Color.parseColor("#2E7D32"))
                holder.tvEstado.setBackgroundResource(R.drawable.bg_badge_verde)
            }

            "vencido" -> {
                holder.tvEstado.text = "✕"
                holder.tvEstado.setTextColor(Color.parseColor("#C62828"))
                holder.tvEstado.setBackgroundResource(R.drawable.bg_badge_rojo)
            }

            "proximo" -> {
                holder.tvEstado.text = "!"
                holder.tvEstado.setTextColor(Color.parseColor("#E65100"))
                holder.tvEstado.setBackgroundResource(R.drawable.bg_badge_naranjaclaro)
            }
        }

        holder.itemView.setOnClickListener { onClick(item) }
    }
}