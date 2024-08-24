package com.example.recipeapp.RcvAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.RandomMealAPI.Meal
import com.example.recipeapp.databinding.CategoriesMealItemsBinding

class FavMealsRcvAdapter(): RecyclerView.Adapter<FavMealsRcvAdapter.FavMealsViewHolder>()  {
    lateinit var onItemClick: ((Meal) -> (Unit))

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>(){  //Improve performance
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    class FavMealsViewHolder(val binding: CategoriesMealItemsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavMealsViewHolder {
        return FavMealsViewHolder(CategoriesMealItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavMealsViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.mealPic)
        holder.binding.mealName.text = meal.strMeal
        holder.binding.mealName.isSelected = true

        holder.itemView.setOnClickListener{
            onItemClick.invoke(meal)
        }
    }
}