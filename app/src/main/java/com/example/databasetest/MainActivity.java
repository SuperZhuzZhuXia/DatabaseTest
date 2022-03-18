package com.example.databasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.nio.file.attribute.BasicFileAttributeView;

/**
 * Android 为了让我们更方便的管理数据库，专门提供了一个 SQLiteOpenHelper 帮助类，借助这个类就可以非常简单的对数据库进行创建和升级。
 *
 *  SQLiteOpenHelper 是一个抽象类，这意味着我们想要使用它的话，就需要创建一个自己的帮助类去继承它，SQLiteOpenHelper 中有两个抽象方法，分别是：onCreate()  onUpgrade()，
 *  我们必须在自己的帮助类里重写这两个方法，然后分别在这两个方法中去实现创建、升级数据库的逻辑。
 *
 *  SQLiteOpenHelper 类中还有两个非常重要的实例方法：getReadableDatabase() 和 getWritableDatabase() 。这两个方法都可以创建或打开一个现有的数据库（如果数据库已存在则
 *  直接打开，否则创建一个新的数据库），并返回一个可对数据库进行读写的对象。不同的是，当数据库不可写入的时候（如磁盘空间已满），getReadableDatabase() 方法返回的对象将
 *  以只读的方式去打开数据库，而 getWritableDatabase() 方法则将出现异常。
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabaseHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);

        Button button1 = (Button)findViewById(R.id.create_database);
        Button button2 = (Button)findViewById(R.id.add_data);
        Button button3 = (Button)findViewById(R.id.update_data);
        Button button4 = (Button)findViewById(R.id.delete_data);
        Button button5 = (Button)findViewById(R.id.query_data);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDatabaseHelper.getWritableDatabase();
            }
        });
//        增
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                //开始组装第一条数据
                contentValues.put("name","The Da Vinci Code");
                contentValues.put("author","Dan Brown");
                contentValues.put("pages",66);
                contentValues.put("price",88);
                db.insert("Book",null,contentValues);
                //开始组装第二条数据
                contentValues.put("name","The Lost Symbol");
                contentValues.put("author","Dan Brown");
                contentValues.put("pages",555);
                contentValues.put("price",99);
                db.insert("Book",null,contentValues);
            }
        });
//        改
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("price",78);

//                这里第三个参数对应的是SQL语句的where部分，表示更新所有name等于？的行，而？是一个占位符，可以通过第四个参数提供
//                的一个字符串数组为第三个参数中的占位符指定相应内容。
                //第一个参数是 ContentValues 对象，第三、四个参数用来指定具体更新哪几行
                db.update("Book",contentValues,"name = ?",new String[]{"The Da Vinci Code"});
            }
        });
//        删
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
//                通过第二、三个参数来指定仅删除那些页数大于500的书
                db.delete("Book","pages > ?",new String[]{"500"});
            }
        });
//        查！！
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
//                 查询Book表中所有的数据
                Cursor cursor = db.query("Book",null,null,null,null,null,null);//7个参数
                if (cursor.moveToFirst()){//将数据的指针移动到第一行的位置
                    do {
                        //遍历Cursor对象，取出数据并打印
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity","book name is "  + name);
                        Log.d("MainActivity","book author is "  + author);
                        Log.d("MainActivity","book pages is "  + pages);
                        Log.d("MainActivity","book price is "  + price);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }
}
///data/data/com.example.databasetest/databases