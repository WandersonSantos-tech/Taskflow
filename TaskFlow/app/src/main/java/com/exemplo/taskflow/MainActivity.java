package com.exemplo.taskflow;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAdicionar;
    private TextView tvListaVazia;

    private DatabaseHelper databaseHelper;
    private TarefaAdapter adapter;
    private final List<Tarefa> listaTarefas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewTarefas);
        fabAdicionar = findViewById(R.id.fabAdicionar);
        tvListaVazia = findViewById(R.id.tvListaVazia);

        databaseHelper = new DatabaseHelper(this);

        configurarRecyclerView();
        carregarTarefas();

        fabAdicionar.setOnClickListener(v -> abrirDialogoNovaTarefa());
    }

    private void configurarRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TarefaAdapter(listaTarefas, new TarefaAdapter.OnTarefaClickListener() {
            @Override
            public void onItemClick(Tarefa tarefa, int position) {
                abrirDialogoEditarTarefa(tarefa);
            }

            @Override
            public void onDeleteClick(Tarefa tarefa, int position) {
                deletarTarefa(tarefa.getId(), position);
            }

            @Override
            public void onStatusClick(Tarefa tarefa, int position, boolean isChecked) {
                tarefa.setStatus(isChecked ? 1 : 0);
                int linhas = databaseHelper.atualizarTarefa(tarefa);

                if (linhas > 0) {
                    Toast.makeText(MainActivity.this, "Status atualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao atualizar status", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void carregarTarefas() {
        listaTarefas.clear();
        listaTarefas.addAll(databaseHelper.listarTarefas());
        adapter.notifyDataSetChanged();
        atualizarEstadoListaVazia();
    }

    private void abrirDialogoNovaTarefa() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tarefa, null);

        EditText etTitulo = view.findViewById(R.id.etTitulo);
        EditText etDescricao = view.findViewById(R.id.etDescricao);

        builder.setTitle("Nova tarefa")
                .setView(view)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String titulo = etTitulo.getText().toString().trim();
                    String descricao = etDescricao.getText().toString().trim();

                    if (titulo.isEmpty()) {
                        Toast.makeText(this, "Digite um título", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Tarefa tarefa = new Tarefa(titulo, descricao, 0);
                    long resultado = databaseHelper.inserirTarefa(tarefa);

                    if (resultado != -1) {
                        Toast.makeText(this, "Tarefa cadastrada", Toast.LENGTH_SHORT).show();
                        carregarTarefas();
                    } else {
                        Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void abrirDialogoEditarTarefa(Tarefa tarefa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tarefa, null);

        EditText etTitulo = view.findViewById(R.id.etTitulo);
        EditText etDescricao = view.findViewById(R.id.etDescricao);

        etTitulo.setText(tarefa.getTitulo());
        etDescricao.setText(tarefa.getDescricao());

        builder.setTitle("Editar tarefa")
                .setView(view)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String titulo = etTitulo.getText().toString().trim();
                    String descricao = etDescricao.getText().toString().trim();

                    if (titulo.isEmpty()) {
                        Toast.makeText(this, "Digite um título", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    tarefa.setTitulo(titulo);
                    tarefa.setDescricao(descricao);

                    int linhas = databaseHelper.atualizarTarefa(tarefa);

                    if (linhas > 0) {
                        carregarTarefas();
                        Toast.makeText(this, "Tarefa atualizada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deletarTarefa(int id, int position) {
        int linhas = databaseHelper.deletarTarefa(id);

        if (linhas > 0) {
            listaTarefas.remove(position);
            adapter.notifyItemRemoved(position);
            atualizarEstadoListaVazia();
            Toast.makeText(this, "Tarefa removida", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao remover", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarEstadoListaVazia() {
        if (listaTarefas.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvListaVazia.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvListaVazia.setVisibility(View.GONE);
        }
    }
}
