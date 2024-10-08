package com.example.recipeapp.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    //lateinit var  api:MealApiInterface

    val  api:MealApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiInterface::class.java)
    }
}