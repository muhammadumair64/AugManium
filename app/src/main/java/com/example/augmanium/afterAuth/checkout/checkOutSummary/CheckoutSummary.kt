package com.example.augmanium.afterAuth.checkout.checkOutSummary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.checkout.checkOutSummary.viewModel.SummaryViewModel
import com.example.augmanium.databinding.ActivityCheckoutSummaryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutSummary : AppCompatActivity() {
    lateinit var binding: ActivityCheckoutSummaryBinding
    lateinit var database: DatabaseReference
    val viewModel: SummaryViewModel by viewModels<SummaryViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_summary)


        initViews()
    }

    fun initViews(){
        database = FirebaseDatabase.getInstance().reference
        viewModel.viewsOfSummaryScreen(this,binding,database)

    }
}