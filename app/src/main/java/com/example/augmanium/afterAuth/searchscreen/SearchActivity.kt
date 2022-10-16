package com.example.augmanium.afterAuth.searchscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.searchResult.mainClass.SearchResult
import com.example.augmanium.databinding.ActivitySearchBinding
import com.example.augmanium.afterAuth.searchscreen.adapter.SearchAdapter
import com.example.augmanium.afterAuth.searchscreen.viewmodel.SearchViewModel
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    lateinit var context: Context
    lateinit var searchAdapter: SearchAdapter
    lateinit var tinyDB: TinyDB
    val viewModelSearchScreen: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search)
        context = this

        tinyDB = TinyDB(this)
        viewModelSearchScreen.bindingSearchScreen(binding,context)
        viewModelSearchScreen.getArrayList()
        searchAdapter= SearchAdapter(viewModelSearchScreen.searchedArrayList)
        binding.searchRecyclerview.adapter=searchAdapter

        binding.searchText.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                Log.d("SEARCH_SUBMIT",query)
                tinyDB.putString(K.SEARCH_QUERY,query)
                var intent = Intent(this@SearchActivity,SearchResult::class.java)
                startActivity(intent)



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