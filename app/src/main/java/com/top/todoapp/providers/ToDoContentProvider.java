package com.top.todoapp.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class ToDoContentProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.todoprovider";
    static final String PATH = "content://"+ PROVIDER_NAME + "/ToDos";
    static final Uri CONTENT_URI = Uri.parse(PATH);
    private static HashMap<String, String> TODOS_PROJECTION_MAP;
    static final int TODOS = 1;
    static final int TODO_ID = 2;
    static final UriMatcher uriMatcher;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "ToDos", TODOS);
        uriMatcher.addURI(PROVIDER_NAME, "TO_Dos/#", TODO_ID);
    }

    private SQLiteDatabase DB;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public String getType( Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert( Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete( Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update( Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query (Uri uri, String[] projection, String selection,
                         String[] selectionArgs, String sortOrder){
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
