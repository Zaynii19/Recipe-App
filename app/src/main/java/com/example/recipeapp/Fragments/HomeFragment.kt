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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipeapp.CategoriesMealActivity
import com.example.recipeapp.MealCategoryListApi.Category
import com.example.recipeapp.MainActivity
import com.example.recipeapp.R
import com.example.recipeapp.RandomMealAPI.Meal
import com.example.recipeapp.RandomMealActivity
import com.example.recipeapp.RcvAdapter.CategoryRcvAdapter
import com.example.recipeapp.RcvAdapter.PopularRcvAdapter
import com.example.recipeapp.RcvAdapter.SearchRcvAdapter
import com.example.recipeapp.ViewModel.HomeViewModel
import com.example.recipeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private val handler = Handler(Looper.getMainLooper())
    //private val homeMvvm by viewModels<HomeViewModel>()
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var randomMeal: Meal
    private var mealCategory: String = ""
    private lateinit var popularItemAdapter: PopularRcvAdapter
    private lateinit var categoryItemAdapter: CategoryRcvAdapter
    private lateinit var searchItemAdapter: SearchRcvAdapter
    private var isSearch: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeMvvm = (activity as MainActivity).homeMvvm  // initialization

        popularItemAdapter = PopularRcvAdapter()
        categoryItemAdapter = CategoryRcvAdapter()
        searchItemAdapter = SearchRcvAdapter()
    }

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

        setPopularRCV()
        observerCategoryMeal()
        onPopularItemClick()

        setCategoryRCV()
        homeMvvm.getCategoryList()
        observerCategoryList()
        onCategoryItemClick()

        setSearchRCV()
    }

    private fun setSearchBtn() {
        binding.searchButton.setOnClickListener {
            searchItemAdapter.clearList()
            toggleSearchView()
            if (isSearch){
                isSearch = false
                binding.searchRcv.visibility = View.INVISIBLE
                binding.textView3.visibility = View.VISIBLE
                binding.imageCardView.visibility = View.VISIBLE
                binding.textView4.visibility = View.VISIBLE
                binding.popularRcv.visibility = View.VISIBLE
                binding.textView5.visibility = View.VISIBLE
                binding.categoryCardView.visibility = View.VISIBLE
            }else {
                isSearch = true
                binding.searchRcv.visibility = View.VISIBLE
                binding.textView3.visibility = View.INVISIBLE
                binding.imageCardView.visibility = View.INVISIBLE
                binding.textView4.visibility = View.INVISIBLE
                binding.popularRcv.visibility = View.INVISIBLE
                binding.textView5.visibility = View.INVISIBLE
                binding.categoryCardView.visibility = View.INVISIBLE
            }
        }
    }

    private fun setPopularRCV() {
        binding.popularRcv.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemAdapter
        }
    }

    private fun setCategoryRCV() {
        binding.categoryRcv.apply {
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoryItemAdapter
        }
    }

    private fun setSearchRCV() {
        binding.searchRcv.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
            adapter = searchItemAdapter
        }
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
        setSearchBtn()

    }

    private fun performSearch(mealName: String) {
        homeMvvm.getSearchMeal(mealName)
        observerSearchList()
        onSearchMealClick()
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
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner) { meal ->
            if (meal != null) {
                Glide.with(this@HomeFragment)
                    .load(meal.strMealThumb)
                    .into(binding.randomMealPic)

                // Update the randomMeal object
                this.randomMeal = meal

                mealCategory = meal.strCategory!!
                // initialize when meal category is get
                homeMvvm.getCategoryMeal(mealCategory)

            } else {
                Toast.makeText(requireContext(), "Failed to load Random meal", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun onRandomMealClick() {
        binding.randomMealPic.setOnClickListener {
            val mealBottomSheetFragment = BottomSheetFragment()
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info Sheet")
        }
    }

    private fun observerCategoryMeal() {
        homeMvvm.observeCategoryMealLiveData().observe(viewLifecycleOwner) { mealList ->
            if (mealList != null) {
                popularItemAdapter.setMeals(mealList = mealList as ArrayList<com.example.recipeapp.CategoryMealApi.Meal>)
            } else {
                Toast.makeText(requireContext(), "Failed to load Popular meal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onPopularItemClick() {
        popularItemAdapter.onItemClick = { meal: com.example.recipeapp.CategoryMealApi.Meal ->
            val intent = Intent(activity, RandomMealActivity::class.java).apply {
                putExtra(MEAL_ID, meal.idMeal)
                putExtra(MEAL_NAME, meal.strMeal)
                putExtra(MEAL_PIC, meal.strMealThumb)
            }
            startActivity(intent)
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

    private fun observerSearchList() {
        homeMvvm.observeMealSearchLiveData().observe(viewLifecycleOwner) { searchList ->
            if (searchList != null) {
                searchItemAdapter.setCategory(searchList = searchList as ArrayList<com.example.recipeapp.MealSearchByNameApi.Meal>)
            } else {
                Toast.makeText(requireContext(), "Failed to load category", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onSearchMealClick() {
        searchItemAdapter.onItemClick = { meal: com.example.recipeapp.MealSearchByNameApi.Meal ->
            val intent = Intent(activity, RandomMealActivity::class.java).apply {
                putExtra(MEAL_ID, meal.idMeal)
                putExtra(MEAL_NAME, meal.strMeal)
                putExtra(MEAL_PIC, meal.strMealThumb)
            }
            startActivity(intent)
        }
    }

    companion object {
        const val MEAL_ID = "com.example.recipeapp.Fragments.idMeal"
        const val MEAL_NAME = "com.example.recipeapp.Fragments.nameMeal"
        const val MEAL_PIC = "com.example.recipeapp.Fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.recipeapp.Fragments.categoryName"
    }
}