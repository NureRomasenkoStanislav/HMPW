package com.example.pz3

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Post(
    val id: Int,
    val title: String,
    val body: String
)

interface ApiService {
    @GET("posts/2")
    suspend fun getPost(): Post
}

class Level3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level3)

        val fetchButton = findViewById<Button>(R.id.fetchButton)
        val dataTextView = findViewById<TextView>(R.id.dataTextView)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        fetchButton.setOnClickListener {
            dataTextView.text = "Завантаження..."

            lifecycleScope.launch {
                try {
                    val post = apiService.getPost()

                    dataTextView.text = "Успішно отримано дані:\n\n" +
                            "ID поста: ${post.id}\n\n" +
                            "Заголовок:\n${post.title}\n\n" +
                            "Текст поста:\n${post.body}"
                } catch (e: Exception) {
                    dataTextView.text = "Помилка з'єднання: ${e.message}\n" +
                            "Перевірте підключення до інтернету!"
                }
            }
        }
    }
}