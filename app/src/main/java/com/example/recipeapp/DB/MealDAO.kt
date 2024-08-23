package com.example.recipeapp.DB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapp.RandomMealAPI.Meal

//setup room database
@Dao
interface MealDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal:Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeal():LiveData<List<Meal>>
}