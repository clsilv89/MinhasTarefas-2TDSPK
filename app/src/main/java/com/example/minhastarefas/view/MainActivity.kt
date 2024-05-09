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
import com.example.minhastarefas.viewmodel.TarefasViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var viewModel: TarefasViewModel
    val adapter = TarefasAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        viewModel = TarefasViewModel(sharedPrefs)
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
            viewModel.completarTarefa(it)
        }
        adapter.onLongClick = {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Excluir tarefa")
            dialog.setMessage("Tem certeza de que deseja excluir essa tarefa?")
            dialog.setPositiveButton("Sim", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                   viewModel.excluirTarefa(it)
                }
            })
            dialog.setNegativeButton("Não", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    // Não faz nada
                }
            })
            dialog.show()
        }
        setupObservsers()
    }

    private fun setupObservsers() {
        viewModel.listaTarefa.observe(this, {
            adapter.submitList(it)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun adicionaTarefa(descricao: String) {
        viewModel.salvaDadosNoApp(descricao)
        navController.navigateUp()
    }
}