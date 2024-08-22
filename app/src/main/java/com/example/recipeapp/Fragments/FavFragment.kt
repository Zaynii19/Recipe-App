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
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentFavBinding
import com.example.recipeapp.databinding.FragmentHomeBinding

class FavFragment : Fragment() {
    private lateinit var binding: FragmentFavBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavBinding.inflate(layoutInflater, container, false)



        return binding.root
    }



    companion object {
    }
}