package com.example.pz3

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Level2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level2)

        val todoEditText = findViewById<EditText>(R.id.todoEditText)
        val addButton = findViewById<Button>(R.id.addButton)
        val todoListView = findViewById<ListView>(R.id.todoListView)

        val todoList = ArrayList<String>()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todoList)
        todoListView.adapter = adapter

        addButton.setOnClickListener {
            val taskText = todoEditText.text.toString().trim()
            if (taskText.isNotEmpty()) {
                todoList.add(taskText)
                adapter.notifyDataSetChanged()
                todoEditText.text.clear()
            } else {
                Toast.makeText(this, "Будь ласка, введіть текст завдання!", Toast.LENGTH_SHORT).show()
            }
        }

        todoListView.setOnItemClickListener { _, _, position, _ ->
            val currentTask = todoList[position]

            if (!currentTask.contains("[Виконано]")) {
                todoList[position] = "✔ $currentTask [Виконано]"
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Це завдання вже виконано!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}