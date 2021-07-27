package com.example.todo_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    ImageView emptyImageview;
    TextView noTasksInDB;
    ToDoBD myDB;
    ArrayList<String> task_id, task_title, task_date, task_category;
    DataAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2D7698"));
        actionBar.setBackgroundDrawable(colorDrawable);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.fab);
        emptyImageview = findViewById(R.id.emptyImageview);
        noTasksInDB = findViewById(R.id.noDataInDB);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        myDB = new ToDoBD(MainActivity.this);
        task_id = new ArrayList<>();
        task_title = new ArrayList<>();
        task_date = new ArrayList<>();
        task_category = new ArrayList<>();

        storeDataInArrays();

        dataAdapter = new DataAdapter(MainActivity.this, this, task_id, task_title, task_date, task_category);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            emptyImageview.setVisibility(View.VISIBLE);
            noTasksInDB.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                task_id.add(cursor.getString(0));
                task_title.add(cursor.getString(1));
                task_date.add(cursor.getString(2));
                task_category.add(cursor.getString(3));
            }
            emptyImageview.setVisibility(View.GONE);
            noTasksInDB.setVisibility(View.GONE);
        }

    }
}