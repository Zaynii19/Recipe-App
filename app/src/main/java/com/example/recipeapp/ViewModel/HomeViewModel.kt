package com.example.recipeapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.RandomMealAPI.Meal
import com.example.recipeapp.RandomMealAPI.MealList
import com.example.recipeapp.Retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Separate logic from main fragment or activity view
class HomeViewModel: ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()

    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, e: Throwable) {
                Log.d("HomeFragment", "onFailure: ${e.message}")
            }

        })
    }

    //use from home fragment to listen live data
    fun observeLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }
}