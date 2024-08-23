package com.example.recipeapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.DB.MealDB
import com.example.recipeapp.RandomMealAPI.Meal
import com.example.recipeapp.RandomMealAPI.RandomMeals
import com.example.recipeapp.Retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RandomMealActivityViewModel(private  val mealDatabase: MealDB): ViewModel() {
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

    // Insert meal to database
    fun insertMeal(meal: Meal) {
         viewModelScope.launch {
             mealDatabase.mealDao().upsert(meal)
         }
    }

    // delete meal from database
    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

}