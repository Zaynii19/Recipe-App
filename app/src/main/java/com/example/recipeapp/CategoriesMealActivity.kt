package com.example.recipeapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.CategoryMealApi.Meal
import com.example.recipeapp.Fragments.HomeFragment
import com.example.recipeapp.RcvAdapter.CategoryMealsRcvAdapter
import com.example.recipeapp.ViewModel.HomeViewModel
import com.example.recipeapp.databinding.ActivityCategoriesMealBinding

class CategoriesMealActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCategoriesMealBinding.inflate(layoutInflater)
    }
    private lateinit var categoryMealAdapter: CategoryMealsRcvAdapter
    private var categoryName = ""
    private val homeMvvm by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryMealAdapter = CategoryMealsRcvAdapter()

        setRCV()

        val intent = intent
        categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME) ?: run {
            Toast.makeText(this, "Category Name is missing", Toast.LENGTH_SHORT).show()
            return
        }

        // initialize when meal category is get
        homeMvvm.getCategoryMeal(categoryName)
        observerCategoryMeal()

    }

    private fun setRCV() {
        binding.mealsRcv.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealAdapter
        }
    }

    private fun observerCategoryMeal() {
        homeMvvm.observeCategoryMealLiveData().observe(this) { mealList ->
            if (mealList != null) {
                categoryMealAdapter.setMeals(mealList = mealList as ArrayList<Meal>)
                binding.categoryCount.text = mealList.size.toString()
            } else {
                Toast.makeText(this@CategoriesMealActivity, "Failed to load meal by category", Toast.LENGTH_SHORT).show()
            }
        }
    }
}