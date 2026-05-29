package com.example.pz3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

data class Recipe(
    val title: String,
    val ingredients: String,
    val instructions: String
)

class Level4Activity : AppCompatActivity() {

    private val recipeList = ArrayList<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level4)

        val recipeTitle = findViewById<EditText>(R.id.recipeTitle)
        val recipeIngredients = findViewById<EditText>(R.id.recipeIngredients)
        val recipeInstructions = findViewById<EditText>(R.id.recipeInstructions)
        val saveRecipeButton = findViewById<Button>(R.id.saveRecipeButton)
        val recipesDisplay = findViewById<TextView>(R.id.recipesDisplay)

        saveRecipeButton.setOnClickListener {
            val title = recipeTitle.text.toString().trim()
            val ingredients = recipeIngredients.text.toString().trim()
            val instructions = recipeInstructions.text.toString().trim()

            if (title.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()) {

                val newRecipe = Recipe(title, ingredients, instructions)
                recipeList.add(newRecipe)

                updateRecipesUi(recipesDisplay)

                recipeTitle.text.clear()
                recipeIngredients.text.clear()
                recipeInstructions.text.clear()

                Toast.makeText(this, "Рецепт успішно додано!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Заповніть абсолютно усі поля!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateRecipesUi(displayTextView: TextView) {
        val builder = StringBuilder()

        for ((index, recipe) in recipeList.withIndex()) {
            builder.append("${index + 1}. СТРАВА: ${recipe.title.uppercase()}\n")
            builder.append("   Інгредієнти: ${recipe.ingredients}\n")
            builder.append("   Приготування: ${recipe.instructions}\n")
            builder.append("--------------------------------------------------\n\n")
        }

        displayTextView.text = builder.toString()
    }
}