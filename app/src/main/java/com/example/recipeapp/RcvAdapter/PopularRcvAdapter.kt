package com.example.recipeapp.RcvAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.CategoryMealApi.Meal
import com.example.recipeapp.databinding.PopularItemsBinding

class PopularRcvAdapter():RecyclerView.Adapter<PopularRcvAdapter.popularViewHolder>() {
    private var mealList = ArrayList<Meal>()
    lateinit var onItemClick: ((Meal) -> (Unit))

    class popularViewHolder(val binding:PopularItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): popularViewHolder {
        return popularViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: popularViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.popularMealPic)

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