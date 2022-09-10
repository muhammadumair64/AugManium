package com.example.augmanium.afterAuth.orderHistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.orderHistory.viewModel.OrderHistoryViewModel
import com.example.augmanium.databinding.ActivityOrderHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderHistory : AppCompatActivity() {

    lateinit var binding: ActivityOrderHistoryBinding
    val viewModel: OrderHistoryViewModel by viewModels<OrderHistoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_history)

        viewModel.orderHistoryRv(binding)
    }
}