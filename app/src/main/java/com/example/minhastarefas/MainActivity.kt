package com.example.minhastarefas

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minhastarefas.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPrefs: SharedPreferences
    private val listaTarefas = arrayListOf<Tarefa>()
    private val categorias = arrayListOf<String>()
    private val gson = GsonBuilder().create()
    val adapter = TarefasAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id)
        navHostFragment?.let { navHostFragment ->
            navController = navHostFragment.findNavController()
        }

        setupActionBarWithNavController(navController)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.navigateUp()) {
                    navController.navigateUp()
                } else {
                    this@MainActivity.finish()
                }
            }
        })
        recuperarDadosNoApp()
        adapter.onClick  = {
            val index = listaTarefas.indexOf(it)
            listaTarefas[index].completa = !listaTarefas[index].completa
            salvaDadosNoApp(listaTarefas)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun adicionaTarefa(descricao: String) {
        listaTarefas.add(
            Tarefa(
                descricao = descricao,
                completa = false
            )
        )
        println(listaTarefas)
        categorias.addAll(categorizaTarefa(descricao))
        println(categorias)
        navController.navigateUp()
        salvaDadosNoApp(listaTarefas)
    }

    private fun categorizaTarefa(descricao: String): List<String> {
        val listaPalavras = descricao.trim().split("\\s+".toRegex())
        val hashTags = arrayListOf<String>()
        for (i in listaPalavras) {
            if (i.contains("#")) {
                if (!categorias.contains(i)) {
                    hashTags.add(i)
                }
            }
        }
        return hashTags
    }

    private fun recuperarDadosNoApp() {
        val jsonString = sharedPrefs.getString("TAREFAS", "")
        val lista = gson.fromJson(jsonString, Array<Tarefa>::class.java)

        listaTarefas.addAll(lista)
        adapter.submitList(listaTarefas)
    }

    private fun salvaDadosNoApp(tarefas: List<Tarefa>) {
        adapter.submitList(tarefas)
        val jsonString = gson.toJson(tarefas)
        sharedPrefs.edit().putString("TAREFAS", jsonString).apply()
    }
}