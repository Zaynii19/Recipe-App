package com.example.recipeapp

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.recipeapp.Fragments.HomeFragment
import com.example.recipeapp.ViewModel.RandomMealActivityViewModel
import com.example.recipeapp.databinding.ActivityRandomMealBinding

class RandomMealActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRandomMealBinding.inflate(layoutInflater)
    }
    private var mealId:String = ""
    private var mealName:String = ""
    private var mealPic:String = ""
    private var mealCategory: String = ""
    private var mealArea: String = ""
    private var mealInstructions: String = ""
    private var youtubeLink: String = ""

    private val mealMvvm by viewModels<RandomMealActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getMealInfo()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()

        onYoutubeBtnClick()

    }

    private fun getMealInfo() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID) ?: run {
            showErrorAndExit("RandomMeal ID is missing")
            return
        }
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME) ?: run {
            showErrorAndExit("RandomMeal Name is missing")
            return
        }
        mealPic = intent.getStringExtra(HomeFragment.MEAL_PIC) ?: run {
            showErrorAndExit("RandomMeal Picture is missing")
            return
        }
    }

    private fun showErrorAndExit(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun observerMealDetailsLiveData() {
        mealMvvm.observeLiveData().observe(this) { value ->
            if (value != null) {
                onResponseCase()

                mealCategory = value.strCategory
                mealArea = value.strArea
                mealInstructions = value.strInstructions
                youtubeLink = value.strYoutube

                setInfoToView() // Update the UI after the data is set
            } else {
                showErrorAndExit("Failed to load meal details")
            }
        }
    }


    private fun setInfoToView() {
        if (mealPic.isNotEmpty() && mealName.isNotEmpty()) {
            Glide.with(applicationContext)
                .load(mealPic)
                .into(binding.mealPic)

            binding.collapsToolbar.title = mealName
            binding.collapsToolbar.setCollapsedTitleTextColor(Color.WHITE)
            binding.collapsToolbar.setExpandedTitleColor(Color.WHITE)

            binding.categoryText.text = buildString {
                append("Category: ")
                append(mealCategory)
            }
            binding.locationText.text = buildString {
                append("Area: ")
                append(mealArea)
            }
            binding.instructionText.text = mealInstructions

        } else {
            showErrorAndExit("RandomMeal data is incomplete")
        }
    }


    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE

        binding.favBtn.visibility = View.INVISIBLE
        binding.instructionTitle.visibility = View.INVISIBLE
        binding.instructionText.visibility = View.INVISIBLE
        binding.categoryText.visibility = View.INVISIBLE
        binding.locationText.visibility = View.INVISIBLE
        binding.youtubeBtn.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE

        binding.favBtn.visibility = View.VISIBLE
        binding.instructionTitle.visibility = View.VISIBLE
        binding.instructionText.visibility = View.VISIBLE
        binding.categoryText.visibility = View.VISIBLE
        binding.locationText.visibility = View.VISIBLE
        binding.youtubeBtn.visibility = View.VISIBLE
    }

    private fun onYoutubeBtnClick() {
        binding.youtubeBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }
}