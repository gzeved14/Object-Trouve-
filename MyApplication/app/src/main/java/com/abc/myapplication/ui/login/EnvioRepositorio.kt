package com.abc.myapplication.ui.login

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.database.getBlobOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import java.io.ByteArrayOutputStream

class EnvioRepositorio (contexto: Context) {

    val helperEnvio : EnvioSQLHelper

    init {
        helperEnvio = EnvioSQLHelper(contexto)
    }

    fun inserir(envio: Envio): Long{
        val db = helperEnvio.writableDatabase

        val valores = ContentValues()
        valores.put(EnvioSQLHelper.COLUNA_TITULO, envio.titulo)
        valores.put(EnvioSQLHelper.COLUNA_DESCRICAO, envio.descricao)
        valores.put(EnvioSQLHelper.COLUNA_CONTATO, envio.contato)
        val stream = ByteArrayOutputStream()
        if(envio.foto!=null)
            envio.foto!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
        valores.put(EnvioSQLHelper.COLUNA_FOTO, stream.toByteArray())
        val id = db.insert(EnvioSQLHelper.NOME_TABELA, null, valores)
        db.close()
        return id
    }

    fun atualizar(envio: Envio): Int{
        val db = helperEnvio.writableDatabase

        val valores = ContentValues()
        valores.put(EnvioSQLHelper.COLUNA_TITULO, envio.titulo)
        valores.put(EnvioSQLHelper.COLUNA_DESCRICAO, envio.descricao)
        valores.put(EnvioSQLHelper.COLUNA_CONTATO, envio.contato)
        val stream = ByteArrayOutputStream()
        if(envio.foto!=null)
            envio.foto!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
        valores.put(EnvioSQLHelper.COLUNA_FOTO, stream.toByteArray())
        val numLinhasAfetadas = db.update(EnvioSQLHelper.NOME_TABELA,
            valores,
            "${EnvioSQLHelper.COLUNA_ID} = ?",
            arrayOf<String>("${envio.id.toString()}"))
        db.close()
        return numLinhasAfetadas
    }

    fun excluir(envio: Envio): Int{
        val db = helperEnvio.writableDatabase
        val numLinhasAfetadas = db.delete(
            EnvioSQLHelper.NOME_TABELA,
            "${EnvioSQLHelper.COLUNA_ID} = ?",
            arrayOf<String>("${envio.id.toString()}"))
        db.close()
        return numLinhasAfetadas
    }

    fun buscarEnvio(nome: String?): List<Envio>{
        val db = helperEnvio.readableDatabase
        var sql ="SELECT * FROM ${EnvioSQLHelper.NOME_TABELA} "
        var argumentos: Array<String>? = null
        if(nome!=null){
            sql += " WHERE ${EnvioSQLHelper.COLUNA_TITULO} LIKE ? "
            argumentos = arrayOf<String>("$nome")
        }
        sql += " ORDER BY ${EnvioSQLHelper.COLUNA_TITULO}"

        val cursor = db.rawQuery(sql, argumentos)

        val envios: MutableList<Envio> = ArrayList()
        while(cursor.moveToNext()){
            var id = cursor.getLongOrNull(cursor.getColumnIndex(EnvioSQLHelper.COLUNA_ID))
            var titulo = cursor.getStringOrNull(cursor.getColumnIndex(EnvioSQLHelper.COLUNA_TITULO))
            var descricao = cursor.getStringOrNull(cursor.getColumnIndex(EnvioSQLHelper.COLUNA_DESCRICAO))
            var contato = cursor.getStringOrNull(cursor.getColumnIndex(EnvioSQLHelper.COLUNA_CONTATO))
            var byteArray = cursor.getBlobOrNull(cursor.getColumnIndex(EnvioSQLHelper.COLUNA_FOTO))
            val foto = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
            val envio = Envio(id!!, titulo!!, descricao!!, contato!!, foto)
           envios.add(envio)
        }
        cursor.close()
        db.close()
        return envios
    }

}