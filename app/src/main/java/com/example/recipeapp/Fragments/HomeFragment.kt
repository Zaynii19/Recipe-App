package com.example.recipeapp.Fragments

import android.content.Intent
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.RandomMealAPI.Meal
import com.example.recipeapp.RandomMealActivity
import com.example.recipeapp.ViewModel.HomeViewModel
import com.example.recipeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private val handler = Handler(Looper.getMainLooper())
    private val homeMvvm by viewModels<HomeViewModel>()
    private lateinit var randomMeal: Meal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSearchView()

        // Fetch the random meal
        homeMvvm.getRandomMeal()

        // Observe the LiveData
        observerRandomMeal()

        onRandomMealClick()
    }

    private fun setSearchView() {
        // Change text color to white of search view
        binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)?.apply {
            setTextColor(Color.BLACK)
            setHintTextColor(Color.GRAY)
        }

        // Get app color from colors.xml
        val appColor = ContextCompat.getColor(requireContext(), R.color.app_color)

        binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)?.setColorFilter(appColor)
        binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)?.setColorFilter(appColor)

        // Set up SearchView with debounce
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({ performSearch(newText.orEmpty()) }, 300)
                binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)?.visibility = View.GONE
                return true
            }
        })

        // Handle Search Button click
        binding.searchButton.setOnClickListener {
            toggleSearchView()
        }
    }

    private fun performSearch(orEmpty: String) {

    }

    private fun toggleSearchView() {
        if (binding.searchView.visibility == View.VISIBLE) {
            collapseSearchView()
        } else {
            binding.searchView.visibility = View.VISIBLE
            binding.searchView.requestFocus()
            binding.searchView.animate().alpha(1f).setDuration(300).setListener(null)
            binding.searchButton.setImageResource(R.drawable.close)
        }
    }

    private fun collapseSearchView() {
        binding.searchView.setQuery("", false) // Clear the query and do not submit it
        binding.searchView.animate().alpha(0f).setDuration(300).withEndAction {
            binding.searchView.visibility = View.GONE
            binding.searchView.clearFocus()
        }
        binding.searchButton.setImageResource(R.drawable.search)
    }

    private fun observerRandomMeal() {
        homeMvvm.observeLiveData().observe(viewLifecycleOwner) { meal ->
            if (meal != null) {
                Glide.with(this@HomeFragment)
                    .load(meal.strMealThumb)
                    .into(binding.randomMealPic)

                // Update the randomMeal object
                this.randomMeal = meal
            } else {
                Toast.makeText(requireContext(), "Failed to load meal", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun onRandomMealClick() {
        binding.randomMealPic.setOnClickListener {
            val intent = Intent(activity, RandomMealActivity::class.java).apply {
                putExtra(MEAL_ID, randomMeal.idMeal)
                putExtra(MEAL_NAME, randomMeal.strMeal)
                putExtra(MEAL_PIC, randomMeal.strMealThumb)
            }
            startActivity(intent)
        }
    }

    companion object {
        const val MEAL_ID = "com.example.recipeapp.Fragments.idMeal"
        const val MEAL_NAME = "com.example.recipeapp.Fragments.nameMeal"
        const val MEAL_PIC = "com.example.recipeapp.Fragments.thumbMeal"
    }
}