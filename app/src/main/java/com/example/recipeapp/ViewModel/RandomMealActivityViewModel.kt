package com.example.recipeapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.RandomMealAPI.Meal
import com.example.recipeapp.RandomMealAPI.RandomMeals
import com.example.recipeapp.Retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RandomMealActivityViewModel(): ViewModel() {
    private var randomMealDetailLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id:String){
        RetrofitInstance.api.getMealDetailsById(id).enqueue(object : Callback<RandomMeals>{
            override fun onResponse(call: Call<RandomMeals>, response: Response<RandomMeals>) {
                if (response.body() != null){
                    randomMealDetailLiveData.value = response.body()!!.meals[0]
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<RandomMeals>, e: Throwable) {
                Log.d("RandomMealActivity", "onFailure: ${e.message}")
            }

        })
    }

    //use from Random meal activity to listen live data
    fun observeLiveData(): LiveData<Meal> {
        return randomMealDetailLiveData
    }

}