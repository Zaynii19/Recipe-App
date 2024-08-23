package com.example.recipeapp.Retrofit

import com.example.recipeapp.RandomMealAPI.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiInterface {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealDetailsById(@Query("i") id:String) : Call<MealList>
}