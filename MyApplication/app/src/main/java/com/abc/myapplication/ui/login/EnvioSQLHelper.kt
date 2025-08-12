package com.abc.myapplication.ui.login

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EnvioSQLHelper (context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, VERSAO) {

    companion object {
        private const val DB_NAME = "OBJETROUVE"
        private const val VERSAO = 1
        const val NOME_TABELA = "Publicacao"
        const val COLUNA_ID = "_id"
        const val COLUNA_TITULO = "titulo"
        const val COLUNA_DESCRICAO = "descricao"
        const val COLUNA_CONTATO = "contato"
        const val COLUNA_FOTO = "foto"
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val sql = "CREATE TABLE $NOME_TABELA (" +
                "$COLUNA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUNA_TITULO TEXT NOT NULL, " +
                "$COLUNA_DESCRICAO TEXT, " +
                "$COLUNA_CONTATO TEXT, " +
                "$COLUNA_FOTO BLOB)"
        sqLiteDatabase.execSQL(sql)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $NOME_TABELA")
        onCreate(sqLiteDatabase)
    }


}