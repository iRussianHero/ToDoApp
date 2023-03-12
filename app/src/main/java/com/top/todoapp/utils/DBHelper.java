package com.top.todoapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.top.todoapp.models.Student;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyTest";
    private static final String TABLE_STUDENTS = "Students";
    private static final String COLUM_ID = "id";
    private static final String COLUM_FIRST_NAME = "firstName";
    private static final String COLUM_LAST_NAME = "lastName";
    private static final String COLUM_AGE = "age";


    private final String CREATE_STUDENTS_TABLE_SCRIPT = "CREATE TABLE " + TABLE_STUDENTS +
            "(" + COLUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUM_FIRST_NAME + " TEXT NOT NULL, " +
            COLUM_LAST_NAME + " TEXT NOT NULL, " +
            COLUM_AGE + " INTEGER NOT NULL)";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_STUDENTS_TABLE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(sqLiteDatabase);
    }


    public void addStudent(Student student){
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUM_FIRST_NAME, student.getFirstName());
            values.put(COLUM_LAST_NAME, student.getLastName());
            values.put(COLUM_AGE, student.getAge());
            sqLiteDatabase.insert(TABLE_STUDENTS, null, values);
            sqLiteDatabase.close();
            Log.i("TEST","GOOD");
        }
        catch (Exception ex){

            Log.i("TEST",ex.getMessage());
            ex.printStackTrace();
            //       try {
        }
    }


    public int updateStudent(Student student){
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUM_FIRST_NAME,student.getFirstName());
            values.put(COLUM_LAST_NAME,student.getLastName());
            values.put(COLUM_AGE,student.getAge());
            int result = sqLiteDatabase
                    .update(TABLE_STUDENTS,values,COLUM_ID + " = ?",new String[]{String.valueOf(student.getId())});
            sqLiteDatabase.close();

            return result;
        }catch (Exception ex){

        }
        return -1;
    }

    public  void deleteStudent(Student student){
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.delete(TABLE_STUDENTS,COLUM_ID + " = ?",new String[]{String.valueOf(student.getId())});
            sqLiteDatabase.close();
        }catch (Exception exception){

        }
    }

    public Student getById(int id){
        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor =  sqLiteDatabase.query(TABLE_STUDENTS,
                    new String[]{COLUM_AGE,COLUM_ID,COLUM_LAST_NAME,COLUM_FIRST_NAME},
                    COLUM_ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null,
                    null);

            if (cursor !=null){
                cursor.moveToFirst();
            }

            Student student =
                    new Student(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3));

            sqLiteDatabase.close();
            return student;
        }catch (Exception ex)
        {

        }
        return null;
    }

    public List<Student> getAll(){
        List<Student> list = new ArrayList<>();

        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_STUDENTS,null);
            if (cursor!=null){
                cursor.moveToFirst();

                do {
                    Student student =
                            new Student(
                                    cursor.getInt(0),
                                    cursor.getString(1),
                                    cursor.getString(2),
                                    cursor.getInt(3));
                    list.add(student);
                }while (cursor.moveToNext());

                sqLiteDatabase.close();
            }
        }catch (Exception ex){
            Log.i("eRROR",ex.getMessage());
        }
        return list;
    }


}
