package com.example.minhastarefas.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minhastarefas.model.Tarefa
import com.example.minhastarefas.model.TarefasRepository
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch

class TarefasViewModel(val sharedPreferences: SharedPreferences) : ViewModel() {

    private val repository = TarefasRepository(sharedPreferences)

    private val _listaTarefas: MutableLiveData<List<Tarefa>> = MutableLiveData()
    val listaTarefa: LiveData<List<Tarefa>> get() = _listaTarefas

    init {
        recuperarDadosNoApp()
    }

    fun salvaDadosNoApp(descricao: String) {
        val tarefa = Tarefa(
            descricao,
            false
        )
        val lista = arrayListOf<Tarefa>()
        _listaTarefas.value?.let {
            lista.addAll(it)
        }
        lista.add(tarefa)
        viewModelScope.launch {
            _listaTarefas.postValue(
                repository.salvarDados(lista)
            )
        }
    }

    fun completarTarefa(tarefa: Tarefa) {
        val lista = arrayListOf<Tarefa>()
        _listaTarefas.value?.let {
            lista.addAll(it)
        }
        val index = lista.indexOf(tarefa)
        lista[index].completa = !lista[index].completa
        viewModelScope.launch {
            _listaTarefas.postValue(
                repository.salvarDados(lista)
            )
        }
    }

    fun excluirTarefa(tarefa: Tarefa) {
        val lista = arrayListOf<Tarefa>()
        _listaTarefas.value?.let {
            lista.addAll(it)
        }
        val index = lista.indexOf(tarefa)
        lista.removeAt(index)

        viewModelScope.launch {
            _listaTarefas.postValue(
                repository.salvarDados(lista)
            )
        }
    }

    private fun recuperarDadosNoApp() {
        viewModelScope.launch {
            _listaTarefas.postValue(
                repository.recuperarDados()
            )
        }
    }

    private fun categorizaTarefa(descricao: String): List<String> {
        val listaPalavras = descricao.trim().split("\\s+".toRegex())
        val hashTags = arrayListOf<String>()
        for (i in listaPalavras) {
//            if (i.contains("#")) {
//                if (!categorias.contains(i)) {
//                    hashTags.add(i)
//                }
//            }
        }
        return hashTags
    }
}