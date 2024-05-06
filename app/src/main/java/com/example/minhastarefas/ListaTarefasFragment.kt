package com.example.minhastarefas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minhastarefas.databinding.FragmentListaTarefasBinding

class ListaTarefasFragment : Fragment() {

    private lateinit var binding: FragmentListaTarefasBinding
    private lateinit var adapter: TarefasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaTarefasBinding.inflate(inflater)
        adapter = (activity as MainActivity).adapter

        binding.botaoCriaTarefa.setOnClickListener {
            findNavController().navigate(R.id.action_listarTarefas_to_criarTarefa)
//            criarNovaTarefa.invoke()
        }

        configuraRecyclerView()

        return binding.root
    }

    private fun configuraRecyclerView() {
        binding.recyclewViewListaTarefas.adapter = adapter
        binding.recyclewViewListaTarefas.layoutManager = LinearLayoutManager(context)
    }

//    companion object {
//        private var criarNovaTarefa: () -> Unit = {}
//
//        @JvmStatic
//        fun newInstance(criaTarefa: () -> Unit = {}, param2: String): ListaTarefasFragment {
//            criarNovaTarefa = criaTarefa
//            return ListaTarefasFragment().apply {
//                arguments = Bundle().apply {
//
//                }
//            }
//        }
//    }
}