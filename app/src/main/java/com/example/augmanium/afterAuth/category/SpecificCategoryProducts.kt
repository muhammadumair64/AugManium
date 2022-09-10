package com.example.augmanium.afterAuth.category

import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.category.viewModel.SpecificCategoryProductViewModel
import com.example.augmanium.databinding.ActivitySpecificCategoryProductsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SpecificCategoryProducts : AppCompatActivity() {

    lateinit var binding: ActivitySpecificCategoryProductsBinding
    val viewModel : SpecificCategoryProductViewModel by viewModels<SpecificCategoryProductViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_specific_category_products)

        viewModel.allProductRv(binding,this)
    }
}