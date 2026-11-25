package com.exemplo.taskflow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tarefas.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TAREFAS = "tarefas";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_DESCRICAO = "descricao";
    public static final String COLUMN_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_TAREFAS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITULO + " TEXT, " +
                COLUMN_DESCRICAO + " TEXT, " +
                COLUMN_STATUS + " INTEGER" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAREFAS);
        onCreate(db);
    }

    public long inserirTarefa(Tarefa tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, tarefa.getTitulo());
        values.put(COLUMN_DESCRICAO, tarefa.getDescricao());
        values.put(COLUMN_STATUS, tarefa.getStatus());

        long id = db.insert(TABLE_TAREFAS, null, values);
        db.close();
        return id;
    }

    public List<Tarefa> listarTarefas() {
        List<Tarefa> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TAREFAS + " ORDER BY " + COLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                tarefa.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)));
                tarefa.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)));
                tarefa.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                lista.add(tarefa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    public int atualizarTarefa(Tarefa tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, tarefa.getTitulo());
        values.put(COLUMN_DESCRICAO, tarefa.getDescricao());
        values.put(COLUMN_STATUS, tarefa.getStatus());

        int linhas = db.update(
                TABLE_TAREFAS,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(tarefa.getId())}
        );

        db.close();
        return linhas;
    }

    public int deletarTarefa(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int linhas = db.delete(TABLE_TAREFAS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return linhas;
    }
}
