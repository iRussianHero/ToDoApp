package com.top.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.AdapterView;
import android.widget.TextView;

import com.top.todoapp.models.Tag;
import com.top.todoapp.models.ToDo;
import com.top.todoapp.utils.DbHelper2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        TextView textView = findViewById(R.id.textview1);
        StringBuilder stringBuilder = new StringBuilder(100);

        DbHelper2 dbHelper2 = new DbHelper2(this);

        Tag tag1 = new Tag("Покупки");
        Tag tag2 = new Tag("Важно");
        Tag tag3 = new Tag("Помотреть");
        Tag tag4 = new Tag("Работа");

        int tagId1 = dbHelper2.createTag(tag1);
        int tagId2 = dbHelper2.createTag(tag2);
        int tagId3 = dbHelper2.createTag(tag3);
        int tagId4 = dbHelper2.createTag(tag4);


        ToDo toDo1 = new ToDo("notebook", 0);
        ToDo toDo2 = new ToDo("tv", 0);
        ToDo toDo3 = new ToDo("mobile", 0);
        ToDo toDo4 = new ToDo("call parent", 0);
        ToDo toDo5 = new ToDo("drive", 0);
        ToDo toDo6 = new ToDo("programming", 0);


        int toDoId1 = dbHelper2.createTodo(toDo1, new int[]{tagId1});
        int toDoId2 = dbHelper2.createTodo(toDo2, new int[]{tagId1, tagId3});
        int toDoId3 = dbHelper2.createTodo(toDo3, new int[]{tagId1});
        int toDoId4 = dbHelper2.createTodo(toDo4, new int[]{tagId2});
        int toDoId5 = dbHelper2.createTodo(toDo5, new int[]{tagId4, tagId2});
        int toDoId6 = dbHelper2.createTodo(toDo6, new int[]{tagId4});


        List<Tag> tagList = dbHelper2.getAll();

        for (Tag tag : tagList) {
            stringBuilder.append(tag.getName() + " : ");
            List<ToDo> list = dbHelper2.getAllToDoByTag(tag.getName());
            int count = 1;
            for (ToDo todo : list) {
                stringBuilder.append("\n " + count++ + ") " + todo.getNote() + " \n ");
            }
        }

        textView.setText(stringBuilder.toString());
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