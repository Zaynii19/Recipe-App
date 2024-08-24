package com.example.recipeapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.CategoryMealApi.CategoryMeals
import com.example.recipeapp.DB.MealDB
import com.example.recipeapp.MealCategoryListApi.Category
import com.example.recipeapp.MealCategoryListApi.MealsCategoryList
import com.example.recipeapp.RandomMealAPI.Meal
import com.example.recipeapp.RandomMealAPI.RandomMeals
import com.example.recipeapp.Retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Separate logic from main fragment or activity view
class HomeViewModel(private val mealDatabase: MealDB): ViewModel() {
    private var randomRandomMealLiveData = MutableLiveData<com.example.recipeapp.RandomMealAPI.Meal>()
    private var categoryMealLiveData = MutableLiveData<List<com.example.recipeapp.CategoryMealApi.Meal>>()
    private var mealCategoriesLiveData = MutableLiveData<List<Category>>()
    private var favMealLiveData = mealDatabase.mealDao().getAllMeal()

    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<RandomMeals> {
            override fun onResponse(call: Call<RandomMeals>, response: Response<RandomMeals>) {
                if (response.body() != null){
                    val randomMeal: com.example.recipeapp.RandomMealAPI.Meal = response.body()!!.meals[0]
                    randomRandomMealLiveData.value = randomMeal
                }else{
                    return
                }
            }
            override fun onFailure(call: Call<RandomMeals>, e: Throwable) {
                Log.d("HomeFragment", "onFailure: ${e.message}")
            }

        })
    }

    //use from home fragment to listen live data
    fun observeRandomMealLiveData(): LiveData<com.example.recipeapp.RandomMealAPI.Meal> {
        return randomRandomMealLiveData
    }

    fun getCategoryMeal(category: String) {
        RetrofitInstance.api.getMealFilterByCategory(category).enqueue(object : Callback<CategoryMeals> {
            override fun onResponse(call: Call<CategoryMeals>, response: Response<CategoryMeals>) {
                if (response.body() != null){
                    categoryMealLiveData.value = response.body()!!.meals
                } else{
                    return
                }
            }

            override fun onFailure(call: Call<CategoryMeals>, e: Throwable) {
                Log.d("HomeFragment", "onFailure: ${e.message}")
            }

        })
    }

    //use from home fragment to listen live data
    fun observeCategoryMealLiveData(): MutableLiveData<List<com.example.recipeapp.CategoryMealApi.Meal>> {
        return categoryMealLiveData
    }

    fun getCategoryList() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<MealsCategoryList> {
            override fun onResponse(call: Call<MealsCategoryList>, response: Response<MealsCategoryList>) {
                if (response.body() != null){
                    mealCategoriesLiveData.value = response.body()!!.categories
                } else{
                    return
                }
            }

            override fun onFailure(call: Call<MealsCategoryList>, e: Throwable) {
                Log.d("HomeFragment", "onFailure: ${e.message}")
            }

        })
    }

    //use from home fragment to listen live data
    fun observeMealCategoriesLiveData(): MutableLiveData<List<Category>> {
        return mealCategoriesLiveData
    }

    fun observeFavMealLiveData(): LiveData<List<Meal>>{
        return favMealLiveData
    }

    // delete meal from database
    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    // Insert meal to database again after delete
    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }


}