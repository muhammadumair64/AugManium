package com.example.augmanium.afterAuth.productDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.databinding.ActivityProductDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {

    lateinit var  binding: ActivityProductDetailsBinding
    val viewModel : ProductDetailViewModel by viewModels<ProductDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        viewModel.detailScreenItems(binding,this)
    }
}