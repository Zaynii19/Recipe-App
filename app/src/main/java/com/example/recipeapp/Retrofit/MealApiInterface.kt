package com.example.recipeapp.Retrofit

import com.example.recipeapp.CategoryMealApi.CategoryMeals
import com.example.recipeapp.MealCategoryListApi.MealsCategoryList
import com.example.recipeapp.MealSearchByNameApi.MealByName
import com.example.recipeapp.RandomMealAPI.RandomMeals
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiInterface {
    @GET("random.php")
    fun getRandomMeal(): Call<RandomMeals>

    @GET("lookup.php?")
    fun getMealDetailsById(@Query("i") id:String) : Call<RandomMeals>

    @GET("filter.php?")
    fun getMealFilterByCategory(@Query("c") category:String): Call<CategoryMeals>

    @GET("categories.php")
    fun getCategories(): Call<MealsCategoryList>

    @GET("search.php")
    fun searchMeal(@Query("s") searchQuery: String): Call<MealByName>
}