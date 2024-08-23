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

class RandomMealActivityViewModel(): ViewModel() {
    private var mealDetailLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id:String){
        RetrofitInstance.api.getMealDetailsById(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null){
                    mealDetailLiveData.value = response.body()!!.meals[0]
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, e: Throwable) {
                Log.d("RandomMealActivity", "onFailure: ${e.message}")
            }

        })
    }

    //use from Random meal activity to listen live data
    fun observeLiveData(): LiveData<Meal> {
        return mealDetailLiveData
    }

}