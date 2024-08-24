package com.example.recipeapp.RcvAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.MealSearchByNameApi.Meal
import com.example.recipeapp.databinding.SearchItemsBinding

class SearchRcvAdapter(): RecyclerView.Adapter<SearchRcvAdapter.SearchViewHolder>() {
    private var searchList = ArrayList<Meal>()
    lateinit var onItemClick: ((Meal) -> (Unit))
    class SearchViewHolder(val binding: SearchItemsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(SearchItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return  searchList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(searchList[position].strMealThumb)
            .into(holder.binding.mealPic)

        holder.binding.mealName.text = searchList[position].strMeal
        holder.binding.mealName.isSelected = true
        holder.binding.categoryText.text = searchList[position].strCategory
        holder.binding.locationText.text = searchList[position].strArea

        holder.itemView.setOnClickListener{
            onItemClick.invoke(searchList[position])
        }
    }

    // Access meal list from outside
    fun setCategory(searchList: ArrayList<Meal>){
        this.searchList = searchList
        notifyDataSetChanged()
    }

    fun clearList() {
        this.searchList.clear()
        notifyDataSetChanged()
    }

}