package com.example.recipeapp.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipeapp.CategoriesMealActivity
import com.example.recipeapp.Fragments.HomeFragment.Companion.CATEGORY_NAME
import com.example.recipeapp.MainActivity
import com.example.recipeapp.MealCategoryListApi.Category
import com.example.recipeapp.R
import com.example.recipeapp.RcvAdapter.CategoryRcvAdapter
import com.example.recipeapp.ViewModel.HomeViewModel
import com.example.recipeapp.databinding.FragmentCategoryBinding
import com.example.recipeapp.databinding.FragmentFavBinding

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryItemAdapter: CategoryRcvAdapter
    private lateinit var homeMvvm: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeMvvm = (activity as MainActivity).homeMvvm  // initialization
        categoryItemAdapter = CategoryRcvAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRcv()
        homeMvvm.getCategoryList()
        observerCategoryList()
        onCategoryItemClick()
    }

    private fun setRcv() {
        binding.categoryRcv.apply {
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoryItemAdapter
        }
    }

    private fun observerCategoryList() {
        homeMvvm.observeMealCategoriesLiveData().observe(viewLifecycleOwner) { categoryList ->
            if (categoryList != null) {
                categoryItemAdapter.setCategory(categoryList = categoryList as ArrayList<Category>)
            } else {
                Toast.makeText(requireContext(), "Failed to load category", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onCategoryItemClick() {
        categoryItemAdapter.onItemClick = { mealCategory: Category ->
            val intent = Intent(activity, CategoriesMealActivity::class.java).apply {
                putExtra(CATEGORY_NAME, mealCategory.strCategory)
            }
            startActivity(intent)
        }
    }



}