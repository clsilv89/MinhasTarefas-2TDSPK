package com.example.minhastarefas.view

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minhastarefas.databinding.ActivityMainBinding
import com.example.minhastarefas.model.Tarefa
import com.example.minhastarefas.viewmodel.TarefasViewModel
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPrefs: SharedPreferences
    private val listaTarefas = arrayListOf<Tarefa>()
    private val categorias = arrayListOf<String>()
    private val gson = GsonBuilder().create()
    val adapter = TarefasAdapter()

    val viewModel = TarefasViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        viewModel.sharedPrefs = sharedPrefs
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

        adapter.onClick = {
            val index = listaTarefas.indexOf(it)
            listaTarefas[index].completa = !listaTarefas[index].completa
            viewModel.salvaDadosNoApp(listaTarefas)
        }
        adapter.onLongClick = {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Excluir tarefa")
            dialog.setMessage("Tem certeza de que deseja excluir essa tarefa?")
            dialog.setPositiveButton("Sim", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val index = listaTarefas.indexOf(it)
                    listaTarefas.removeAt(index)
                    viewModel.salvaDadosNoApp(listaTarefas)
                    adapter.notifyDataSetChanged()
                }
            })
            dialog.setNegativeButton("Não", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    // Não faz nada
                }
            })
            dialog.show()
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
        viewModel.salvaDadosNoApp(listaTarefas)
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
        val jsonString = sharedPrefs.getString("TAREFAS", "[]")
        val lista = gson.fromJson(jsonString, Array<Tarefa>::class.java)

        listaTarefas.addAll(lista)
        adapter.submitList(listaTarefas)
    }
}