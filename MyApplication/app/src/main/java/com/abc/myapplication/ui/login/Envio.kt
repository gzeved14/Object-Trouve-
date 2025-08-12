package com.abc.myapplication.ui.login

import android.graphics.Bitmap

data class Envio(
    var id: Long = 0L,
    var titulo: String?,
    var descricao: String?,
    var contato: String?,
    var foto: Bitmap? = null
)
