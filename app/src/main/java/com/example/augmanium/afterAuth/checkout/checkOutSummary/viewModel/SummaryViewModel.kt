package com.example.augmanium.afterAuth.checkout.checkOutSummary.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.augmanium.afterAuth.cartFragment.adapter.CartFragmentAdapter
import com.example.augmanium.afterAuth.cartFragment.dataClass.CartFragmentDataClass
import com.example.augmanium.afterAuth.checkout.checkOutSummary.adapter.SummaryRVAdapter
import com.example.augmanium.afterAuth.checkout.checkOutSummary.dataClass.SummaryDataClass
import com.example.augmanium.databinding.ActivityCheckoutSummaryBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor() : ViewModel() {
    var cartFragmentArrayList: ArrayList<CartFragmentDataClass> = ArrayList()
    var summaryArrayList: ArrayList<SummaryDataClass> = ArrayList()
    lateinit var tinyDB: TinyDB
    fun viewsOfSummaryScreen(context:Context,binding:ActivityCheckoutSummaryBinding){

        tinyDB= TinyDB(context)
viewModelScope.launch {
    cartFragmentArrayList = tinyDB.getListObject(
        K.CART,
        CartFragmentDataClass::class.java
    ) as ArrayList<CartFragmentDataClass>
    Log.d("SummaryScreenTesting","${cartFragmentArrayList.size}")
    for( product in cartFragmentArrayList){
        try {
            Log.d("SummaryScreenTesting","Running : ${ product.image!!},  ${product.productName!!},${product.productPrice!!}")
            summaryArrayList.add(SummaryDataClass(product.image!!, product.productName!!,product.productPrice!!))

        }catch (e: Exception){
            Log.d("SummaryScreenTesting"," Error : $e")

        }
    }


recyclerViewInitializer(binding)
}






    }

    @SuppressLint("NotifyDataSetChanged")
    fun recyclerViewInitializer(binding: ActivityCheckoutSummaryBinding) {
        Log.d("SummaryScreenTesting","In function")

        binding.summaryRV.also {
            it.adapter = SummaryRVAdapter(summaryArrayList)
            it.setHasFixedSize(true)
//            it.adapter!!.notifyDataSetChanged()
        }

    }

}