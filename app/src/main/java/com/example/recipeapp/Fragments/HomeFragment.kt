package com.example.recipeapp.Fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.RandomMealAPI.Meal
import com.example.recipeapp.ViewModel.HomeViewModel
import com.example.recipeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private val handler = Handler(Looper.getMainLooper())
    private val homeMvvm by viewModels<HomeViewModel>()

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

        homeMvvm.getRandomMeal()
        observerRandomMeal()
    }

    private fun observerRandomMeal() {
        homeMvvm.observeLiveData().observe(viewLifecycleOwner, object:Observer<Meal>{
            override fun onChanged(value: Meal) {
                Glide.with(this@HomeFragment)
                    .load(value.strMealThumb)
                    .into(binding.randomMealPic)
            }
        })
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
}