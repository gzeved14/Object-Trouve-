package com.abc.myapplication.ui.login

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class TelaNovoEnvio : AppCompatActivity() {

    var COD_REQ: Int = 101
    var imageBitmap: Bitmap? = null

    lateinit var editTextTitulo: EditText
    lateinit var editTextDescricao: EditText
    lateinit var editTextContato: EditText
    lateinit var imageViewFoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_nova_publicacao)

        editTextTitulo = findViewById<EditText>(R.id.edittext_titulo)
        editTextDescricao = findViewById<EditText>(R.id.edittext_descricao)
        editTextContato = findViewById<EditText>(R.id.edittext_contato)
        imageViewFoto = findViewById<ImageView>(R.id.imgview_foto)

        val it : Intent = intent
        val p = it.getIntExtra("p",-1)
        lateinit var envio: Envio
        var alterar = if(p!=-1) true else false
        if (alterar) {
            envio = EnvioController.getEnvio(p)
            mostraDados(envio)
        }

        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)

        //salvar os envios no banco de dados ou alterar caso ela já exista
        btnConfirmar.setOnClickListener {
            val titulo = editTextTitulo.text.toString()
            val descricao = editTextDescricao.text.toString()
            val contato = editTextContato.text.toString()
            if(titulo.isNotEmpty() && descricao.isNotEmpty() && contato.isNotEmpty()){
                if (!alterar) {
                    envio = Envio(0L, titulo, descricao, contato, imageBitmap)
                    val envioRepositorio = EnvioRepositorio(applicationContext)
                    var id = envioRepositorio.inserir(envio)
                    if(id>=0) {
                        envio.id = id
                        EnvioController.listaDeEnvios().clear()
                        EnvioController.listaDeEnvios().addAll(envioRepositorio.buscarEnvio(null))
                        Toast.makeText(this, "Publicação criada!", Toast.LENGTH_LONG).show()
                    }
                    limpaFormulario()
                }else{
                    envio.titulo = titulo
                    envio.descricao = descricao
                    envio.contato = contato
                    envio.foto = imageBitmap
                    val envioRepositorio = EnvioRepositorio(applicationContext)
                    var numLinhasAfetadas = envioRepositorio.atualizar(envio)
                    if(numLinhasAfetadas>0) {
                        EnvioController.listaDeEnvios().clear()
                        EnvioController.listaDeEnvios().addAll(envioRepositorio.buscarEnvio(null))
                    }
                    Toast.makeText(this, "Alteraçoes salvas!", Toast.LENGTH_LONG).show()
                    limpaFormulario()
                    finish()
                }
            }else{
                editTextTitulo.error = if(titulo.isEmpty()) "Preencha um valor" else null
                editTextDescricao.error = if(descricao.isEmpty()) "Preencha um valor" else null
                editTextContato.error = if(contato.isEmpty()) "Preencha um valor" else null
            }

        }

        // inserir foto
        imageViewFoto.setOnClickListener{
            val intentGaleria = Intent(Intent.ACTION_GET_CONTENT)
            intentGaleria.type = "image/*"
            startActivityForResult(Intent.createChooser(intentGaleria, "Selecione uma imagem"), COD_REQ)
        }

    }

    // função para limpar os campos do formulario
    private fun limpaFormulario() {
        editTextTitulo.text.clear()
        editTextTitulo.requestFocus()
        editTextDescricao.text.clear()
        editTextContato.text.clear()
        imageViewFoto.setImageResource(android.R.drawable.ic_menu_camera)
    }

    // resgatar informações caso publicação for ser alterada
    private fun mostraDados(envio: Envio){
        editTextTitulo.setText(envio.titulo)
        editTextDescricao.setText(envio.descricao)
        editTextContato.setText(envio.contato)
        imageBitmap = envio.foto
        if(imageBitmap != null)
            imageViewFoto.setImageBitmap(imageBitmap)
        else
            imageViewFoto.setImageResource(android.R.drawable.ic_menu_camera)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == COD_REQ && resultCode == Activity.RESULT_OK){
            if (data != null && data.data != null){
                var inputStream = contentResolver.openInputStream(data.data!!)
                imageBitmap = BitmapFactory.decodeStream(inputStream)
                imageViewFoto.setImageBitmap(imageBitmap)
            }
        }else{
            Toast.makeText(this, "Escolha uma imagem", Toast.LENGTH_LONG).show()
        }
    }

}