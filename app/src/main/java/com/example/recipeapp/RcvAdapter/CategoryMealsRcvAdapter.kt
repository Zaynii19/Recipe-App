package com.example.recipeapp.RcvAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.CategoryMealApi.Meal
import com.example.recipeapp.databinding.CategoriesMealItemsBinding

class CategoryMealsRcvAdapter(): RecyclerView.Adapter<CategoryMealsRcvAdapter.CategoryMealsViewHolder>()  {
    private var mealList = ArrayList<Meal>()
    lateinit var onItemClick: ((Meal) -> (Unit))

    class CategoryMealsViewHolder(val binding: CategoriesMealItemsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        return CategoryMealsViewHolder(CategoriesMealItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.mealPic)

        holder.binding.mealName.text = mealList[position].strMeal

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealList[position])
        }
    }

    // Access meal list from outside
    fun setMeals(mealList: ArrayList<Meal>){
        this.mealList = mealList
        notifyDataSetChanged()
    }
}