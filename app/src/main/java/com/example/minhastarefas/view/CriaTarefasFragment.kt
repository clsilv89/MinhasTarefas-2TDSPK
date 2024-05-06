package com.example.minhastarefas.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.minhastarefas.databinding.FragmentCriaTarefasBinding

class CriaTarefasFragment : Fragment() {

    private lateinit var binding: FragmentCriaTarefasBinding
    private var descricaoTarefa = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCriaTarefasBinding.inflate(inflater)

        binding.editTextTarefa.addTextChangedListener {
//            println(it.toString())
            descricaoTarefa = it.toString()
        }

        binding.botaoCriaTarefa.setOnClickListener {
            if (descricaoTarefa.isNotEmpty()) {
                (activity as MainActivity).adicionaTarefa(descricaoTarefa)
            } else {
                binding.textInputLayoutTarefa.error = "Descrição da tarefa não pode estar vazia!"
            }
//            tarefa(descricaoTarefa)
        }

        return binding.root
    }

//    companion object {
//        private var tarefa: (String) -> Unit = {}
//        @JvmStatic
//        fun newInstance(tarefa: (String) -> Unit = {}, param2: String): CriaTarefasFragment {
//            this.tarefa = tarefa
//            return CriaTarefasFragment().apply {
//                arguments = Bundle().apply {
//
//                }
//            }
//        }
//    }
}