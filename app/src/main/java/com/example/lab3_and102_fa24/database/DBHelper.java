package com.example.lab3_and102_fa24.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper( Context context) {
        super(context, "TodoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL = "CREATE TABLE TASKS(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, CONTENT TEXT, DATE TEXT, TYPE TEXT, STATUS INTEGER)";
        db.execSQL(SQL);

        String SQL_Insert = "INSERT INTO TASKS(ID, TITLE, CONTENT, DATE,TYPE, STATUS) VALUES" +
                "('1', 'AND', 'Hoc AND co ban', '11/07/2024', 'De', '0')," +
                "('2', 'PHP', 'Hoc PHP co ban', '12/07/2024', 'De', '1')," +
                "('3', 'Python', 'Hoc Python co ban', '11/07/2024', 'De', '0')," +
                "('4', 'JS', 'Hoc JS co ban', '11/07/2024', 'De', '1')";
        db.execSQL(SQL_Insert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        if (oldversion != newversion){
            db.execSQL("DROP TABLE IF EXISTS TASKS");
            onCreate(db);
        }

    }
}
