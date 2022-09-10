package com.example.augmanium.afterAuth.orderHistory.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.augmanium.afterAuth.mainActivity.Adapter.AllProductViewAdapter
import com.example.augmanium.afterAuth.orderHistory.adapter.OrderHistoryAdapter
import com.example.augmanium.afterAuth.orderHistory.dataClass.OrderHistoryDataClass
import com.example.augmanium.afterAuth.search.searchDataClass.AllProductDataClass
import com.example.augmanium.databinding.ActivityOrderHistoryBinding
import com.example.augmanium.databinding.ActivitySearchResultBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(): ViewModel() {

    val orderHistoryArrayList: ArrayList<OrderHistoryDataClass> = ArrayList()

    fun orderHistoryRv(binding: ActivityOrderHistoryBinding) {
        orderHistoryArrayList.add(OrderHistoryDataClass("Order No: 9980713", "1200$", "9 Jun , 2022 9:57 PM"))
        orderHistoryArrayList.add(OrderHistoryDataClass("Order No: 9980713", "1200$", "9 Jun , 2022 9:57 PM"))
        orderHistoryArrayList.add(OrderHistoryDataClass("Order No: 9980713", "1200$", "9 Jun , 2022 9:57 PM"))
        orderHistoryArrayList.add(OrderHistoryDataClass("Order No: 9980713", "1200$", "9 Jun , 2022 9:57 PM"))
        orderHistoryArrayList.add(OrderHistoryDataClass("Order No: 9980713", "1200$", "9 Jun , 2022 9:57 PM"))

        binding.orderHistoryRv.also {
            it.adapter = OrderHistoryAdapter(orderHistoryArrayList)
            it.setHasFixedSize(true)
        }
    }

}