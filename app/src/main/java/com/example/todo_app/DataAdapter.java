package com.example.todo_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList task_id, task_title, task_date, task_category;

    DataAdapter(Activity activity, Context context, ArrayList task_id, ArrayList task_title, ArrayList task_date, ArrayList task_category) {
        this.activity = activity;
        this.context = context;
        this.task_id = task_id;
        this.task_title = task_title;
        this.task_date = task_date;
        this.task_category = task_category;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_element, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.MyViewHolder holder, int position) {
        holder.task_id_text.setText(String.valueOf(task_id.get(position)));
        holder.task_title_text.setText(String.valueOf(task_title.get(position)));
        holder.task_date_text.setText(String.valueOf(task_date.get(position)));
        holder.task_category_text.setText(String.valueOf(task_category.get(position)));
    }

    @Override
    public int getItemCount() {
        return task_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView task_id_text, task_title_text, task_date_text, task_category_text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            task_id_text = itemView.findViewById(R.id.taskID);
            task_title_text = itemView.findViewById(R.id.taskTitleFromDB);
            task_date_text = itemView.findViewById(R.id.taskDateFromDB);
            task_category_text = itemView.findViewById(R.id.taskCategoryFromDB);
        }
    }
}