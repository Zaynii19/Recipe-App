package com.example.recipeapp.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipeapp.RandomMealAPI.Meal

// create Room Database
@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(MealTypeConverter::class)
abstract class MealDB: RoomDatabase() {
    abstract fun mealDao(): MealDAO

    companion object {
        @Volatile
        var INSTANCE: MealDB? = null

        @Synchronized
        fun getInstance(context: Context): MealDB{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDB::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }

            return  INSTANCE as MealDB
        }
    }
}