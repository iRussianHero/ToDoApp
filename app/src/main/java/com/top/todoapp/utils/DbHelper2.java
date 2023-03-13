package com.top.todoapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.top.todoapp.models.Tag;
import com.top.todoapp.models.ToDo;

import java.util.ArrayList;
import java.util.List;

public class DbHelper2 extends SQLiteOpenHelper {

    public DbHelper2(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "StepsToSuccess";

    // table names
    private static final String TABLE_TODO = "ToDos";
    private static final String TABLE_Tags = "Tags";
    private static final String TABLE_TODO_TAG = "ToDosTags";

    //column names
    private  static final String KEY_ID="id";
    private  static final String KEY_TODO="todo";
    private  static final String KEY_STATUS="status";
    private  static final String KEY_TAG_NAME="tagName";
    private  static final String KEY_TODO_ID="id_todo";
    private  static final String KEY_TAG_ID="id_tag";


    //scripts for create tables
    private final String CREATE_TABLE_TODO_SCRIPT = "CREATE TABLE " + TABLE_TODO +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_TODO + " TEXT NOT NULL, " +
            KEY_STATUS + " TEXT NOT NULL)";

    private final String CREATE_TABLE_TAG_SCRIPT = "CREATE TABLE " + TABLE_Tags +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_TAG_NAME + " TEXT NOT NULL)";

    private final String CREATE_TABLE_TODO_TAG_SCRIPT = "CREATE TABLE " + TABLE_TODO_TAG +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_TAG_ID + " INTEGER," +
            KEY_TODO_ID + " INTEGER)";



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TAG_SCRIPT);
        sqLiteDatabase.execSQL(CREATE_TABLE_TODO_SCRIPT);
        sqLiteDatabase.execSQL(CREATE_TABLE_TODO_TAG_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Tags);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_TAG);
        onCreate(sqLiteDatabase);
    }


    ////////////////////////////////////////////////////////////////////////
    //  methods
    ////////////////////////////////////////////////////////////////////////


    ///Tag

    public int createTag(Tag tag){
        int result = -1;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_TAG_NAME, tag.getName());
            result = (int) sqLiteDatabase.insert(TABLE_Tags, null, values);
            sqLiteDatabase.close();
        }
        catch (Exception ex){

            Log.i("TEST",ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    public int updateTag(Tag tag){
        int result = -1;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_TAG_NAME, tag.getName());
            result = (int) sqLiteDatabase.update(TABLE_Tags, values,KEY_ID + " = ? " , new String[]{String.valueOf(tag.getId())});
            sqLiteDatabase.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public  void deleteTag(Tag tag,boolean shouldDeleteAllTagTodos){
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

            if (shouldDeleteAllTagTodos){
                List<ToDo> list = getAllToDoByTag(tag.getName());

                for (ToDo item :list) {
                    deleteTodo(item);
                }
            }

            sqLiteDatabase.delete(TABLE_Tags,KEY_ID + " = ?",new String[]{String.valueOf(tag.getId())});
            sqLiteDatabase.close();
        }catch (Exception exception){

        }
    }

    public List<Tag> getAll(){
        List<Tag> list = new ArrayList<>();

        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_Tags,null);
            if (cursor!=null){
                cursor.moveToFirst();

                do {
                    Tag tag =
                            new Tag(
                                    cursor.getInt(0),
                                    cursor.getString(1));
                    list.add(tag);
                }while (cursor.moveToNext());

                sqLiteDatabase.close();
            }
        }catch (Exception ex){
        }
        return list;
    }


    //////////  Todo

    public int createTodo(ToDo toDo,int[] tagIds){
        int result = -1;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_TODO, toDo.getNote());
            values.put(KEY_STATUS, toDo.getStatus());
            result = (int) sqLiteDatabase.insert(TABLE_TODO, null, values);

            for (int tagId:tagIds) {
                createToDoTag(result,tagId);
            }

            sqLiteDatabase.close();
        }
        catch (Exception ex){

            Log.i("TEST",ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }


    public ToDo getToDoById(int id){
        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_TODO + " WHERE " + KEY_ID + " = ?",
                    new String[]{String.valueOf(id)});


            //?????????????????????????????????????????????/
            if (cursor !=null){
                cursor.moveToFirst();

                ToDo toDo =
                        new ToDo(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getInt(2));

                sqLiteDatabase.close();
                return toDo;
            }

        }catch (Exception ex)
        {

        }
        return null;
    }



    public List<ToDo> getAllToDo(){
        List<ToDo> list = new ArrayList<>();

        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_TODO,null);
            if (cursor!=null){
                cursor.moveToFirst();

                do {
                    ToDo toDo =
                            new ToDo(
                                    cursor.getInt(0),
                                    cursor.getString(1),
                                    cursor.getInt(1));
                    list.add(toDo);
                }while (cursor.moveToNext());

                sqLiteDatabase.close();
            }
        }catch (Exception ex){
        }
        return list;
    }



    public List<ToDo> getAllToDoByTag(String tagName){
        List<ToDo> list = new ArrayList<>();

        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_TODO +
                    " td, " + TABLE_Tags + " tg, " +
                    TABLE_TODO_TAG + " tt WHERE tg." + KEY_TAG_NAME + " = ? AND tg." + KEY_ID +
                    " = tt."+ KEY_TAG_ID + " AND td." + KEY_ID + " = tt." + KEY_TODO_ID,new String[]{tagName});
            if (cursor!=null){
                cursor.moveToFirst();

                do {
                    ToDo toDo =
                            new ToDo(
                                    cursor.getInt(0),
                                    cursor.getString(1),
                                    cursor.getInt(1));
                    list.add(toDo);
                }while (cursor.moveToNext());

                sqLiteDatabase.close();
            }
        }catch (Exception ex){
        }
        return list;
    }

    public int updateTodo(ToDo toDo){
        int result = -1;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_TODO, toDo.getNote());
            values.put(KEY_STATUS, toDo.getStatus());
            result = (int) sqLiteDatabase.update(TABLE_TODO,  values,KEY_ID + " = ? " , new String[]{String.valueOf(toDo.getId())});

            sqLiteDatabase.close();
        }
        catch (Exception ex){
        }
        return result;
    }

    public int deleteTodo(ToDo toDo){
        int result = -1;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

            result = sqLiteDatabase.delete(TABLE_TODO,KEY_ID + " = ? " , new String[]{String.valueOf(toDo.getId())});

            sqLiteDatabase.close();
        }
        catch (Exception ex){

            Log.i("TEST",ex.getMessage());
            ex.printStackTrace();
            //       try {
        }
        return result;
    }


    ///////TODO_TAG

    public  int createToDoTag(int toDoId,int tagId){
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_TAG_ID,tagId);
            contentValues.put(KEY_TODO_ID,toDoId);
            int result = (int) sqLiteDatabase.insert(TABLE_TODO_TAG,null,contentValues);
            sqLiteDatabase.close();
            return result;
        }catch (Exception ex){

        }
        return -1;
    }
}
