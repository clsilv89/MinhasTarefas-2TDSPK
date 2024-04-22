package com.example.minhastarefas

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minhastarefas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val listaTarefas = arrayListOf<Tarefa>()
    private val categorias = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
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
        Toast.makeText(this, recuperarDadosNoApp(), Toast.LENGTH_SHORT).show()
//        abrirTelaListaTarefas()
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

    private fun recuperarDadosNoApp(): String {
        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)

        return sharedPrefs.getString("TAREFA", "").orEmpty()
    }

    private fun salvaDadosNoApp(tarefas: List<Tarefa>) {
        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)

        sharedPrefs.edit().putString("TAREFA", tarefas[0].descricao).apply()
    }

//    override fun onBackPressed() {
//        navController.navigateUp()
//    }

//    private fun abrirTelaListaTarefas() {
//        val listaTarefasFragment = ListaTarefasFragment.newInstance({
//            abrirTelaCriarTarefas()
//        }, "")
//
//        supportFragmentManager.beginTransaction().replace(
//            binding.frameLayout.id,
//            listaTarefasFragment
//        ).commit()
//    }
//
//    private fun abrirTelaCriarTarefas() {
//        val listaTarefas = arrayListOf<Tarefa>()
//        val criaTarefasFragment = CriaTarefasFragment.newInstance({
//            val tarefa = Tarefa(
//                descricao = it,
//                completa = false
//            )
//            listaTarefas.add(tarefa)
//            println(tarefa)
//        }, "")
//
//        supportFragmentManager.beginTransaction().replace(
//            binding.frameLayout.id,
//            criaTarefasFragment
//        ).commit()
//    }
}