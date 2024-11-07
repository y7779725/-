package com.example.xangmu5;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class sql extends SQLiteOpenHelper {


    private static String DBNAME = "zsmemo.db";
    private static int VERSIO = 1;

    public sql(@Nullable Context context) {
        super(context, DBNAME, null,VERSIO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_memory(_id Integer primary key,title String(2000),content String(2000),imgpath String(2000),mtime String(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
