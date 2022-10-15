package com.example.augmanium.afterAuth.checkout.checkOutSummary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.augmanium.R
import com.example.augmanium.afterAuth.cartFragment.CartFragmentViewModel
import com.example.augmanium.afterAuth.checkout.checkOutSummary.viewModel.SummaryViewModel
import com.example.augmanium.databinding.ActivityCheckOutAddressBinding
import com.example.augmanium.databinding.ActivityCheckoutSummaryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutSummary : AppCompatActivity() {
    lateinit var binding: ActivityCheckoutSummaryBinding
    val viewModel: SummaryViewModel by viewModels<SummaryViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_summary)


        initViews()
    }

    fun initViews(){

        viewModel.viewsOfSummaryScreen(this,binding)

    }
}