package com.example.socialproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialproject.data.PostEntity
import com.example.socialproject.data.MessageEntity
import com.example.socialproject.data.SocialDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = SocialDatabase.getDatabase(applicationContext)
        val dao = db.socialDao()

        if (dao.getAllPosts().isEmpty()) {
            dao.insertPost(PostEntity(author = "Марія", text = "Всім привіт! Як вам Jetpack Compose?"))
            dao.insertPost(PostEntity(author = "Андрій", text = "Kotlin — це топ для мобільної розробки."))
        }

        setContent {
            MaterialTheme {
                MainScreen(dao)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(dao: com.example.socialproject.data.SocialDao) {
    val context = LocalContext.current

    var currentScreen by remember { mutableStateOf("feed") }

    var posts by remember { mutableStateOf(dao.getAllPosts()) }
    var messages by remember { mutableStateOf(dao.getAllMessages()) }

    var searchQuery by remember { mutableStateOf("") }
    var newPostText by remember { mutableStateOf("") }
    var newMessageText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MySocial App", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1565C0))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (currentScreen == "feed") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Пошук друзів або постів...") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val query = searchQuery.lowercase()
                        if (query.isNotEmpty()) {
                            val filteredCount = posts.count {
                                it.author.lowercase().contains(query) || it.text.lowercase().contains(query)
                            }
                            Toast.makeText(context, "Знайдено збігів: $filteredCount", Toast.LENGTH_LONG).show()
                        }
                    }) {
                        Text("Пошук")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newPostText,
                    onValueChange = { newPostText = it },
                    label = { Text("Що нового, Станіславе?") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (newPostText.isNotEmpty()) {
                            dao.insertPost(PostEntity(author = "Станіслав", text = newPostText))
                            newPostText = ""
                            posts = dao.getAllPosts()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Опублікувати")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Стрічка новин:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(posts) { post ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = post.author, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = post.text)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "❤️ ${post.likes}", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }

                Button(
                    onClick = { currentScreen = "chat" },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Відкрити Особисті Повідомлення")
                }

            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Чат (Особисті)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    TextButton(onClick = { currentScreen = "feed" }) {
                        Text("< Назад до стрічки")
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(messages) { msg ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            contentAlignment = if (msg.sender == "Станіслав") Alignment.CenterEnd else Alignment.CenterStart
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(
                                        color = if (msg.sender == "Станіслав") Color(0xFFDCF8C6) else Color(0xFFECEFF1),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Text(text = msg.sender, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(text = msg.text, fontSize = 16.sp)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newMessageText,
                        onValueChange = { newMessageText = it },
                        placeholder = { Text("Повідомлення...") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (newMessageText.isNotEmpty()) {
                            dao.insertMessage(
                                MessageEntity(
                                    sender = "Станіслав",
                                    text = newMessageText,
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                            newMessageText = ""
                            messages = dao.getAllMessages()
                        }
                    }) {
                        Text("Послати")
                    }
                }
            }
        }
    }
}