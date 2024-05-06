package com.example.minhastarefas.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.minhastarefas.model.Tarefa
import com.google.gson.GsonBuilder

class TarefasViewModel() : ViewModel() {

    init {
        recuperarDadosNoApp()
    }

    private val gson = GsonBuilder().create()
    var sharedPrefs: SharedPreferences? = null
    val listaTarefas = arrayListOf<Tarefa>()
    fun salvaDadosNoApp(tarefas: List<Tarefa>) {
//        adapter.submitList(tarefas)
        val jsonString = gson.toJson(tarefas)
        sharedPrefs?.edit()?.putString("TAREFAS", jsonString)?.apply()
    }

    private fun recuperarDadosNoApp() {
        val jsonString = sharedPrefs?.getString("TAREFAS", "[]")
        val lista = gson.fromJson(jsonString, Array<Tarefa>::class.java)

        listaTarefas.addAll(lista)
//        adapter.submitList(listaTarefas)
    }
}