package com.example.recipeapp.Retrofit

import com.example.recipeapp.RandomMealAPI.MealList
import retrofit2.Call
import retrofit2.http.GET

interface RandomMealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>
}