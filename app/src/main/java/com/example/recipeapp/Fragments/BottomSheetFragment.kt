package com.example.recipeapp.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.recipeapp.Fragments.HomeFragment.Companion
import com.example.recipeapp.Fragments.HomeFragment.Companion.MEAL_NAME
import com.example.recipeapp.Fragments.HomeFragment.Companion.MEAL_PIC
import com.example.recipeapp.MainActivity
import com.example.recipeapp.R
import com.example.recipeapp.RandomMealAPI.Meal
import com.example.recipeapp.RandomMealActivity
import com.example.recipeapp.ViewModel.HomeViewModel
import com.example.recipeapp.databinding.FragmentBottomSheetBinding
import com.example.recipeapp.databinding.FragmentCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetBinding
    private lateinit var homeMvvm : HomeViewModel
    private lateinit var randomMeal: Meal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeMvvm = (activity as MainActivity).homeMvvm

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch the random meal
        homeMvvm.getRandomMeal()
        // Observe the LiveData
        observerRandomMeal()
        onReadMoreClick()
    }

    private fun observerRandomMeal() {
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner) { meal ->
            if (meal != null) {
                Glide.with(this@BottomSheetFragment)
                    .load(meal.strMealThumb)
                    .into(binding.mealPic)

                binding.mealName.text = meal.strMeal
                binding.mealName.isSelected = true
                binding.categoryText.text = meal.strCategory
                binding.locationText.text = meal.strArea

                // Update the randomMeal object
                this.randomMeal = meal

            } else {
                Toast.makeText(requireContext(), "Failed to load Random meal", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun onReadMoreClick() {
        binding.readMore.setOnClickListener {
            val intent = Intent(activity, RandomMealActivity::class.java).apply {
                putExtra(HomeFragment.MEAL_ID, randomMeal.idMeal)
                putExtra(MEAL_NAME, randomMeal.strMeal)
                putExtra(MEAL_PIC, randomMeal.strMealThumb)
            }
            startActivity(intent)
            dismiss()
        }
    }

    companion object {
        const val MEAL_ID = "com.example.recipeapp.Fragments.idMeal"
        const val MEAL_NAME = "com.example.recipeapp.Fragments.nameMeal"
        const val MEAL_PIC = "com.example.recipeapp.Fragments.thumbMeal"
    }
}