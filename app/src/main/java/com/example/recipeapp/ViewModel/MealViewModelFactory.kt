package com.example.recipeapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.DB.MealDB

class MealViewModelFactory(private val mealDatabase: MealDB): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RandomMealActivityViewModel(mealDatabase) as T
    }
}