package com.abc.myapplication.ui.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.text.NumberFormat
import java.util.*

class EnvioAdapter (contexto: Context) : ArrayAdapter<Envio>(contexto, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val v: View
        if(convertView != null) {
            v = convertView
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.list_view_item, parent, false)
        }

        val item = getItem(position)

        val textViewTitulo = v.findViewById<TextView>(R.id.textview_titulo)
        val textViewDescricao = v.findViewById<TextView>(R.id.textview_descricao)
        val textViewContato = v.findViewById<TextView>(R.id.textview_contato)
        val imgFoto = v.findViewById<ImageView>(R.id.img_item_foto)

        textViewTitulo.text = item?.titulo
        textViewDescricao.text = item?.descricao.toString()
        textViewContato.text = item?.contato.toString()

        if(item?.foto != null){
            imgFoto.setImageBitmap(item?.foto)
        }
        return v
    }


}