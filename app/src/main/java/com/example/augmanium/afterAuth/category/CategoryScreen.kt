package com.example.augmanium.afterAuth.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.category.viewModel.CategoryScreenViewModel
import com.example.augmanium.databinding.ActivityCategoryScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryScreen : AppCompatActivity() {

    lateinit var binding: ActivityCategoryScreenBinding
    val viewModel : CategoryScreenViewModel by viewModels<CategoryScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_category_screen)
        viewModel.categoryScreenItems(binding,this)
    }
}