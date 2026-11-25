package com.exemplo.taskflow;

public class Tarefa {
    private int id;
    private String titulo;
    private String descricao;
    private int status; // 0 = pendente, 1 = concluída

    public Tarefa() {
    }

    public Tarefa(String titulo, String descricao, int status) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
    }

    public Tarefa(int id, String titulo, String descricao, int status) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", status=" + status +
                '}';
    }
}
