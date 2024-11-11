package com.example.lab3_and102_fa24.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lab3_and102_fa24.database.DBHelper;
import com.example.lab3_and102_fa24.models.TaskInfo;

import java.util.ArrayList;

public class TaskInfoDAO {
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public TaskInfoDAO(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long addInfo(TaskInfo inFo){
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("title", inFo.getTitle());
        values.put("content", inFo.getContent());
        values.put("date", inFo.getDate());
        values.put("type",inFo.getType());

        long check = database.insert("TASKS",null, values);
        if(check <= 0){
            return -1;
        }
        return 1;
    }

    public long updateInfo(TaskInfo inFo){
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id",inFo.getId());
        values.put("title", inFo.getTitle());
        values.put("content", inFo.getContent());
        values.put("date", inFo.getDate());
        values.put("type",inFo.getType());
        values.put("status", inFo.getStatus());

        long check = database.update("TASKS", values, "id=?", new String[]{String.valueOf(inFo.getId())});
        if(check <= 0){
            return -1;
        }
        return 1;
    }

    public ArrayList<TaskInfo> getListInfo(){
        ArrayList<TaskInfo> list = new ArrayList<>();
        database = dbHelper.getReadableDatabase();
        try{
            Cursor cursor = database.rawQuery("SELECT * FROM TASKS", null);
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                do {
                    list.add(new TaskInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5)));

                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            Log.e("ERROR", e.getMessage());
        }
        return list;
    }

    public boolean removeInfo(int id){
        int row = database.delete("TASKS", "id=?", new String[]{String.valueOf(id)});
        return row != -1;
    }

    public boolean updateTypeInfo(int id, boolean check){
        int status = check ?1:0;
        ContentValues values = new ContentValues();
        values.put("status", status);
        long row = database.update("TASKS", values, "id=?", new String[]{String.valueOf(id)});
        return row !=-1;
    }
}
