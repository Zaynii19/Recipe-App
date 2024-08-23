package com.example.recipeapp.Fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.InlistMealCategoryApi.Category
import com.example.recipeapp.MainActivity
import com.example.recipeapp.R
import com.example.recipeapp.RandomMealAPI.Meal
import com.example.recipeapp.RcvAdapter.FavMealsRcvAdapter
import com.example.recipeapp.RcvAdapter.PopularRcvAdapter
import com.example.recipeapp.ViewModel.HomeViewModel
import com.example.recipeapp.databinding.FragmentFavBinding
import com.example.recipeapp.databinding.FragmentHomeBinding

class FavFragment : Fragment() {
    private lateinit var binding: FragmentFavBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var favItemAdapter: FavMealsRcvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeMvvm = (activity as MainActivity).homeMvvm  // initialization
        favItemAdapter = FavMealsRcvAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRCV()
        observeFavMeals()
    }

    private fun setRCV() {
        binding.favRcv.apply {
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
            adapter = favItemAdapter
        }
    }

    private fun observeFavMeals() {
        homeMvvm.observeFavMealLiveData().observe(requireActivity(), Observer { meals ->
            if (meals != null) {
                favItemAdapter.differ.submitList(meals)
            } else {
                Toast.makeText(requireContext(), "Failed to load favorite meals", Toast.LENGTH_SHORT).show()
            }
        })
    }
}