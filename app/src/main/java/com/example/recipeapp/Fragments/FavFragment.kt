package com.example.recipeapp.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.Fragments.HomeFragment.Companion.MEAL_ID
import com.example.recipeapp.Fragments.HomeFragment.Companion.MEAL_NAME
import com.example.recipeapp.Fragments.HomeFragment.Companion.MEAL_PIC
import com.example.recipeapp.MainActivity
import com.example.recipeapp.RandomMealAPI.Meal
import com.example.recipeapp.RandomMealActivity
import com.example.recipeapp.RcvAdapter.FavMealsRcvAdapter
import com.example.recipeapp.ViewModel.HomeViewModel
import com.example.recipeapp.databinding.FragmentFavBinding
import com.google.android.material.snackbar.Snackbar

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

        val itemTouchHelper = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            // on rcv swipe up and down
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

            // on rcv item swipe right and left
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = favItemAdapter.differ.currentList[position]
                // Remove the item from the list
                homeMvvm.deleteMeal(deletedMeal)
                Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo"
                ) {
                    // Re-insert the deleted item
                    homeMvvm.insertMeal(deletedMeal)
                }.show()
            }
        }


        setRCV()
        observeFavMeals()
        onFavMealClick()
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favRcv)
    }

    private fun setRCV() {
        binding.favRcv.apply {
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, true)
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

    private fun onFavMealClick() {
        favItemAdapter.onItemClick = { meal: Meal ->
            val intent = Intent(activity, RandomMealActivity::class.java).apply {
                putExtra(MEAL_ID, meal.idMeal)
                putExtra(MEAL_NAME, meal.strMeal)
                putExtra(MEAL_PIC, meal.strMealThumb)
            }
            startActivity(intent)
        }
    }
}