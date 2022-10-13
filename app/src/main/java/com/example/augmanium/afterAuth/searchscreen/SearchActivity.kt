package com.example.augmanium.afterAuth.searchscreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.databinding.ActivitySearchBinding
import com.example.augmanium.afterAuth.searchscreen.adapter.SearchAdapter
import com.example.augmanium.afterAuth.searchscreen.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    lateinit var context: Context
    lateinit var searchAdapter: SearchAdapter
    val viewModelSearchScreen: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search)
        context = this

        viewModelSearchScreen.bindingSearchScreen(binding,context)
        viewModelSearchScreen.getArrayList()
        searchAdapter= SearchAdapter(viewModelSearchScreen.searchedArrayList)
        binding.searchRecyclerview.adapter=searchAdapter

        binding.searchText.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchAdapter.filter.filter(newText)
                println("New text${newText.length}")
                return false
            }
        })
    }




}