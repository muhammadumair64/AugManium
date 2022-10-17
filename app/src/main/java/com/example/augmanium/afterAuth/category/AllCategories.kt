package com.example.augmanium.afterAuth.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.category.viewModel.AllCategoriesViewModel
import com.example.augmanium.databinding.ActivityAllCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCategories : AppCompatActivity() {

    lateinit var binding: ActivityAllCategoriesBinding
    val viewModel: AllCategoriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this,R.layout.activity_all_categories)

        viewModel.allCategoryRv(binding,this)



    }
}