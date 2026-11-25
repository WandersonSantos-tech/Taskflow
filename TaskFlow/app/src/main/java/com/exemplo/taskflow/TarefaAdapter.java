package com.exemplo.taskflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder> {

    public interface OnTarefaClickListener {
        void onItemClick(Tarefa tarefa, int position);
        void onDeleteClick(Tarefa tarefa, int position);
        void onStatusClick(Tarefa tarefa, int position, boolean isChecked);
    }

    private final List<Tarefa> listaTarefas;
    private final OnTarefaClickListener listener;

    public TarefaAdapter(List<Tarefa> listaTarefas, OnTarefaClickListener listener) {
        this.listaTarefas = listaTarefas;
        this.listener = listener;
    }

    public void atualizarLista(List<Tarefa> novasTarefas) {
        listaTarefas.clear();
        listaTarefas.addAll(novasTarefas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarefa, parent, false);
        return new TarefaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        Tarefa tarefa = listaTarefas.get(position);

        holder.tvTitulo.setText(tarefa.getTitulo());
        holder.tvDescricao.setText(tarefa.getDescricao());
        holder.checkBoxStatus.setChecked(tarefa.getStatus() == 1);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(tarefa, position);
            }
        });

        holder.btnExcluir.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(tarefa, position);
            }
        });

        holder.checkBoxStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onStatusClick(tarefa, position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTarefas == null ? 0 : listaTarefas.size();
    }

    static class TarefaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;
        TextView tvDescricao;
        CheckBox checkBoxStatus;
        ImageButton btnExcluir;

        public TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloTarefa);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoTarefa);
            checkBoxStatus = itemView.findViewById(R.id.checkBoxStatus);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
        }
    }
}
