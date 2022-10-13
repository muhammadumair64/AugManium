package com.example.augmanium.afterAuth.search.mainClass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.mainActivity.viewModel.SearchResultViewModel
import com.example.augmanium.databinding.ActivitySearchResultBinding

class SearchResult : AppCompatActivity() {

    lateinit var binding: ActivitySearchResultBinding
    val viewModel: SearchResultViewModel by viewModels<SearchResultViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result)
    viewModel.screenBinding(this,binding)
    }
}