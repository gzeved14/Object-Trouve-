package com.abc.myapplication.ui.login

object EnvioController {

    private val ENVIOS: MutableList<Envio> = arrayListOf()

    fun cadastrar(envio: Envio){
        ENVIOS.add(envio)
    }

    fun listaDeEnvios() = ENVIOS

    fun getEnvio(i: Int) = ENVIOS.get(i)

    fun atualiza(i: Int, envio: Envio){
        ENVIOS.set(i, envio)
    }

    fun apaga(i: Int){
        ENVIOS.removeAt(i)
    }


}