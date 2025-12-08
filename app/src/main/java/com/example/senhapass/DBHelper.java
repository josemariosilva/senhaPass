package com.example.senhapass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "passwords.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE passwords (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS passwords");
        onCreate(db);
    }

    // INSERT
    public long addPassword(String title, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("password", password);
        return db.insert("passwords", null, cv);
    }

    // SELECT ALL
    public List<PasswordModel> getAllPasswords() {
        List<PasswordModel> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM passwords", null);

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndexOrThrow("id"));
                String desc = c.getString(c.getColumnIndexOrThrow("title"));
                String pass = c.getString(c.getColumnIndexOrThrow("password"));
                lista.add(new PasswordModel(id, desc, pass));
            } while (c.moveToNext());
        }
        c.close();
        return lista;
    }

    // DELETE
    public void deletePassword(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("passwords", "id=?", new String[]{String.valueOf(id)});
    }

    // UPDATE — agora funcionando!
    public boolean updatePassword(int id, String desc, String sen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", desc);
        cv.put("password", sen);

        int rows = db.update("passwords", cv, "id=?", new String[]{String.valueOf(id)});
        return rows > 0;  // true se atualizou
    }
}
