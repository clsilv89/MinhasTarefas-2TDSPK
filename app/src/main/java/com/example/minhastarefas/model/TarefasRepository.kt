package com.example.minhastarefas.model

import android.content.SharedPreferences
import com.google.gson.GsonBuilder

class TarefasRepository(val sharedPrefs: SharedPreferences) {

    val gson = GsonBuilder().create()

    suspend fun recuperarDados() : List<Tarefa> {
        val jsonString = sharedPrefs.getString(TAREFAS, "[]")

        val lista = gson.fromJson(
            jsonString,
            Array<Tarefa>::class.java
        )
        return lista.toList()
    }

    suspend fun salvarDados(listaTarefas: List<Tarefa>) : List<Tarefa> {
        val jsonString = gson.toJson(listaTarefas)

        sharedPrefs.edit().putString(TAREFAS, jsonString).apply()

        return recuperarDados()
    }

    companion object {
        private const val TAREFAS = "TAREFAS"
    }
}