package com.example.todolist.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todolist.model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String name = "toDoListDB";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TO_DO_TABLE = " CREATE TABLE " + TODO_TABLE + "( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, " + STATUS + " INTEGER) ";
    private SQLiteDatabase db;


    public DBHandler(@Nullable Context context) {
        super(context, name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TO_DO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void openDataBase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, task.getTask());
        contentValues.put(STATUS, 0);
        db.insert(TODO_TABLE, null, contentValues);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
            try {
                cursor = db.query(TODO_TABLE,null,null,null,null,
                        null,null,null);
                if (cursor != null){
                    if (cursor.moveToFirst()){
                        do {
                            ToDoModel task = new ToDoModel();
                            task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                            task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                            task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                            taskList.add(task);
                        }while (cursor.moveToNext());
                    }
                }
            }
            finally {
                db.endTransaction();
                cursor.close();
            }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS , status);
        db.update(TODO_TABLE , contentValues , ID + "=?" , new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK,task);
        db.update(TODO_TABLE,contentValues, ID + "=?" ,new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }

}
