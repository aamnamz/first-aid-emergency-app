package com.example.madproject

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CategoriesActivity : AppCompatActivity() {

    private lateinit var searchBar: EditText
    private lateinit var gridCategories: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        // --- 1. TOOLBAR SETUP FIX ---
        // Find the search input EditText from the included toolbar
        val toolbarSearchInput = findViewById<EditText>(R.id.toolbar_search_input)

        // FIX: Pass the required EditText and the lambda callback to the helper
        ToolbarHelper.setupSearchToolbar(this, toolbarSearchInput) { isSearchActive ->
            // Guide/Index activities leave this empty, as they typically don't have a suggestions list
        }
        // --- END TOOLBAR FIX ---

        // Note: The searchBar findViewById here might be redundant if you only use the toolbar's search input.
        // If your layout has a separate search bar, keep this line:
        searchBar = findViewById(R.id.searchBar)

        gridCategories = findViewById(R.id.gridCategories)

        setupCategoryClicks()
    }

    private fun setupCategoryClicks() {
        for (i in 0 until gridCategories.childCount) {
            // Ensure the child is a LinearLayout before casting
            val categoryCard = gridCategories.getChildAt(i)
            if (categoryCard is LinearLayout) {
                categoryCard.setOnClickListener {
                    // Assuming the second child (index 1) is the TextView containing the name
                    val categoryName = (categoryCard.getChildAt(1) as android.widget.TextView).text.toString()

                    when (categoryName) {
                        "Asthma" -> startActivity(Intent(this, AsthmaActivity::class.java))
                        "Burns" -> startActivity(Intent(this, BurnActivity::class.java))
                        "Fainting" -> startActivity(Intent(this, FaintingActivity::class.java))
                        "Choking" -> startActivity(Intent(this, ChokingActivity::class.java))
                        "CPR" -> startActivity(Intent(this, CprActivity::class.java))
                        "Wound" -> startActivity(Intent(this, WoundActivity::class.java))
                        "Fracture" -> startActivity(Intent(this, FractureActivity::class.java))
                        "HeatStroke" -> startActivity(Intent(this, HeatStrokeActivity::class.java))
                        else -> Toast.makeText(this, "Category not available", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}