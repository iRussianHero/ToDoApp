package com.top.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> tasks = new ArrayList<String>();
    ArrayList<String> selectedTasks = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Collections.addAll(tasks, "First", "Second", "Third");
        tasksList = findViewById(R.id.listview1);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, tasks);
        tasksList.setAdapter(adapter);

        tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String user = adapter.getItem(position);
                if (tasksList.isItemChecked(position))
                    selectedTasks.add(user);
                else
                    selectedTasks.remove(user);
            }
        });
    }

    public void add_task(View view) {
        EditText taskName = findViewById(R.id.task_name);
        String task = taskName.getText().toString();
        if (!task.isEmpty()) {
            adapter.add(task);
            taskName.setText("");
            adapter.notifyDataSetChanged();
        }
    }

    public void remove_task(View view) {
        for (int i = 0; i < selectedTasks.size(); i++) {
            adapter.remove(selectedTasks.get(i));
        }

        tasksList.clearChoices();
        selectedTasks.clear();

        adapter.notifyDataSetChanged();
    }
}
