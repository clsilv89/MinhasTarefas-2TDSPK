package com.example.minhastarefas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minhastarefas.databinding.ItemTarefaCompletaBinding
import com.example.minhastarefas.databinding.ItemTarefaNaoCompletaBinding

class TarefasAdapter : ListAdapter<Tarefa, RecyclerView.ViewHolder>(DiffCallback()) {

    var onClick: (Tarefa) -> Unit = {}

    inner class TarefasViewHolder(val binding: ItemTarefaNaoCompletaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tarefa: Tarefa) {
            binding.textViewDescricaoTarefa.text = tarefa.descricao
            binding.root.setOnClickListener {
                notifyDataSetChanged()
                onClick(tarefa)
            }
        }
    }

    inner class TarefasCompletasViewHolder(val binding: ItemTarefaCompletaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tarefa: Tarefa) {
            binding.textViewDescricaoTarefa.text = tarefa.descricao
            binding.root.setOnClickListener {
                notifyDataSetChanged()
                onClick(tarefa)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Tarefa>() {
        override fun areItemsTheSame(oldItem: Tarefa, newItem: Tarefa): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Tarefa,
            newItem: Tarefa
        ): Boolean {
            return oldItem.descricao == newItem.descricao
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).completa) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTarefaNaoCompletaBinding.inflate(layoutInflater, parent, false)
        val bindingCompleta = ItemTarefaCompletaBinding.inflate(layoutInflater, parent, false)
        val tarefasViewHolder = TarefasViewHolder(binding)
        val tarefasCompletasViewHolder = TarefasCompletasViewHolder(bindingCompleta)

        return if (viewType == 0) {
            tarefasViewHolder
        } else {
            tarefasCompletasViewHolder
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TarefasViewHolder -> {
                holder.bind(getItem(position))
            }

            is TarefasCompletasViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }
}