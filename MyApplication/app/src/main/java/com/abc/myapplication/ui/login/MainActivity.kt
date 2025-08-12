package com.abc.myapplication.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    lateinit var envioAdapter: EnvioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Botão novo abre tela de nova publicação
        val btnNovo = findViewById<Button>(R.id.btnNovo)
        btnNovo.setOnClickListener {
            val intentNovo = Intent (this, TelaNovoEnvio::class.java)
            startActivity(intentNovo)
        }

        // Configurando Adapter e Controller
        envioAdapter = EnvioAdapter(this)
        envioAdapter.addAll(EnvioController.listaDeEnvios())

        // Configurando listView
        val listViewPublicacao = findViewById<ListView>(R.id.listview_publicacoes)
        listViewPublicacao.adapter = envioAdapter

        // Alterar publicação ao apertar nela
        listViewPublicacao.setOnItemClickListener { parent, view, position, id ->
            val intentAlterar = Intent(this, TelaNovoEnvio::class.java)
            intentAlterar.putExtra("p",position)
            startActivity(intentAlterar)
        }
         envioAdapter
        // Apagar envio ao manter pressionado
        listViewPublicacao.setOnItemLongClickListener { parent, view, position, id ->
            val envio = EnvioController.getEnvio(position)
            val envioRepositorio = EnvioRepositorio(this)
            val numLinhasAfetadas = envioRepositorio.excluir(envio)
            if(numLinhasAfetadas>0)
                atualizaLista()
            true
        }
    }


    //função para atualizar lista
    fun atualizaLista(){
        val envioRepositorio = EnvioRepositorio(this)
        val publicacoes = envioRepositorio.buscarEnvio(null)
        EnvioController.listaDeEnvios().clear()
        EnvioController.listaDeEnvios().addAll(publicacoes)
        envioAdapter.clear()
        envioAdapter.addAll(EnvioController.listaDeEnvios())
        envioAdapter.notifyDataSetChanged()
    }

    //atualizar lista toda vez que a atividade for retomada
    override fun onResume() {
        super.onResume()
        atualizaLista()
    }

}