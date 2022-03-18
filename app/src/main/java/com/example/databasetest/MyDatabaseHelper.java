package com.example.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK = "create table Book (" + "id integer primary key autoincrement,"
            + "author text,"
            + "price real,"
            + "pages integer,"
            + "name text)";
    public static final String CREATE_CATEGORY = "create table Category (" + "id integer primary key autoincrement,"
            + "category_name text,"
            + "category_code integer)";

    private Context mContext;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK);//使用该方法执行这条建表语句
        sqLiteDatabase.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext,"Create Succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {//对数据库进行升级的方法
        sqLiteDatabase.execSQL("drop table if exists Book");
        sqLiteDatabase.execSQL("drop table if exists Category");
        onCreate(sqLiteDatabase);
    }
}
