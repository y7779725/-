package com.example.xangmu5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    Button button1;
    RecyclerView recyc_view;
    sql sql;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button1 =findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this,MainActivity3.class);
                startActivity(intent);
            }
        });


        recyc_view = findViewById(R.id.recy_view);
        sql = new sql(MainActivity2.this);
        db = sql.getWritableDatabase();

        // 使用 ViewTreeObserver 监听视图树的变化
        recyc_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 这里可以执行需要在视图布局完成后进行的操作

                // 注意：在执行完需要的操作后，记得移除监听器
                recyc_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // 这里执行 RecyclerView 的相关操作
                recyDisplay();
            }
        });
    }

    private void recyDisplay(){
        List<MBdate>arr = new ArrayList<>();
        Cursor cursor= db.rawQuery("select * from tb_memory",null);
        while (cursor.moveToNext()){
            String mt1 = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String mt2 = cursor.getString(cursor.getColumnIndexOrThrow("content"));
            String mt3 = cursor.getString(cursor.getColumnIndexOrThrow("imgpath"));
            String mt4 = cursor.getString(cursor.getColumnIndexOrThrow("mtime"));
            MBdate mBdate = new MBdate(mt1,mt2,mt3,mt4);
            arr.add(mBdate);
        }
        cursor.close();

        MemoryAdapter adapter = new MemoryAdapter(MainActivity2.this,arr);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyc_view.setLayoutManager(staggeredGridLayoutManager);
        recyc_view.setAdapter(adapter);

    }
}