package com.example.senhapass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "passwords.db";
    private static final int DB_VERSION = 2; // ⬆ aumentei a versão para atualizar tabela

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // tabela corrigida
        db.execSQL("CREATE TABLE passwords (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "descricao TEXT," +
                "senha TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Atualiza banco antigo para novo formato
        if (oldVersion == 1) {
            db.execSQL("ALTER TABLE passwords RENAME TO temp_old;");

            db.execSQL("CREATE TABLE passwords (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "descricao TEXT," +
                    "senha TEXT)");

            db.execSQL("INSERT INTO passwords (descricao, senha) " +
                    "SELECT title, password FROM temp_old;");

            db.execSQL("DROP TABLE temp_old;");
        }
    }

    // --------------------------------------------------------------
    // 🔒 FUNÇÕES DE "CRIPTOGRAFIA" (Base64)
    // --------------------------------------------------------------
    private String encode(String s) {
        return Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
    }

    private String decode(String s) {
        return new String(Base64.decode(s, Base64.DEFAULT));
    }

    // --------------------------------------------------------------
    // INSERT
    // --------------------------------------------------------------
    public long addPassword(String desc, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("descricao", desc);
        cv.put("senha", encode(senha)); // 🔒 salvando codificado

        return db.insert("passwords", null, cv);
    }

    // --------------------------------------------------------------
    // SELECT ALL
    // --------------------------------------------------------------
    public List<PasswordModel> getAllPasswords() {
        List<PasswordModel> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM passwords", null);

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndexOrThrow("id"));
                String desc = c.getString(c.getColumnIndexOrThrow("descricao"));
                String passEncoded = c.getString(c.getColumnIndexOrThrow("senha"));

                String senhaDecodificada = decode(passEncoded); // 🔒 decodificando ao exibir

                lista.add(new PasswordModel(id, desc, senhaDecodificada));
            } while (c.moveToNext());
        }

        c.close();
        return lista;
    }

    // --------------------------------------------------------------
    // DELETE
    // --------------------------------------------------------------
    public void deletePassword(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("passwords", "id=?", new String[]{String.valueOf(id)});
    }

    // --------------------------------------------------------------
    // UPDATE
    // --------------------------------------------------------------
    public boolean updatePassword(int id, String desc, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("descricao", desc);
        cv.put("senha", encode(senha)); // 🔒 codificação

        int rows = db.update("passwords", cv, "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }
}
