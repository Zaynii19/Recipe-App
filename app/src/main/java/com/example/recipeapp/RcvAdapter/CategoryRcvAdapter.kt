package com.example.recipeapp.RcvAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.CategoryMealApi.Meal
import com.example.recipeapp.InlistMealCategoryApi.Category
import com.example.recipeapp.databinding.CategoryItemsBinding

class CategoryRcvAdapter(): RecyclerView.Adapter<CategoryRcvAdapter.CategoryViewHolder>() {
    private var categoryList = ArrayList<Category>()
    lateinit var onItemClick: ((Category) -> (Unit))
    class CategoryViewHolder(val binding: CategoryItemsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoryList[position].strCategoryThumb)
            .into(holder.binding.categoryMealPic)

        holder.binding.categoryMealText.text = categoryList[position].strCategory

        holder.itemView.setOnClickListener{
            onItemClick.invoke(categoryList[position])
        }
    }

    // Access meal list from outside
    fun setCategory(categoryList: ArrayList<Category>){
        this.categoryList = categoryList
        notifyDataSetChanged()
    }
}