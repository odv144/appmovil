package com.odvsystem.sportcenter.components

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.odvsystem.sportcenter.R

class Header @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?=null
): LinearLayout(context,attrs){
    private val btnAtras: Button
    private val tvTitulo: TextView
    init {
        inflate(context, R.layout.component_header, this)
        orientation=VERTICAL
        btnAtras = findViewById<Button>(R.id.btnAtras)
        tvTitulo = findViewById<TextView>(R.id.tvTitulo)
    }
    fun setTitulo(titulo:String){
        tvTitulo.text=titulo
    }
    fun setDestino(destino:Class<*>){
        btnAtras.setOnClickListener {
            context.startActivity(Intent(context, destino))
        }
    }
    fun setOnAtrasClick(accion:()->Unit){
        btnAtras.setOnClickListener { accion() }
    }
}
