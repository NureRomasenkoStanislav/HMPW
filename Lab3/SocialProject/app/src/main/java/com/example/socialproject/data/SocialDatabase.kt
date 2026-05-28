package com.example.socialproject.data

import android.content.Context
import androidx.room.*

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val author: String,
    val text: String,
    val likes: Int = 0
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String,
    val text: String,
    val timestamp: Long
)

@Dao
interface SocialDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPosts(): List<PostEntity>

    @Insert
    fun insertPost(post: PostEntity)

    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun getAllMessages(): List<MessageEntity>

    @Insert
    fun insertMessage(message: MessageEntity)
}

@Database(entities = [PostEntity::class, MessageEntity::class], version = 1, exportSchema = false)
abstract class SocialDatabase : RoomDatabase() {
    abstract fun socialDao(): SocialDao

    companion object {
        @Volatile
        private var INSTANCE: SocialDatabase? = null

        fun getDatabase(context: Context): SocialDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SocialDatabase::class.java,
                    "social_db"
                ).allowMainThreadQueries().build() // Дозволяємо швидкі запити для лаби
                INSTANCE = instance
                instance
            }
        }
    }
}