package com.example.pz3

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Level1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level1)

        val welcomeButton = findViewById<Button>(R.id.welcomeButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        welcomeButton.setOnClickListener {
            resultTextView.text = "Вітаємо з першим додатком на Kotlin!"
        }
    }
}