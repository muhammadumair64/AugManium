package com.example.augmanium.afterAuth.mainActivity.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.augmanium.afterAuth.mainActivity.Adapter.AllProductViewAdapter
import com.example.augmanium.afterAuth.mainActivity.Adapter.CatagoryAdapter
import com.example.augmanium.afterAuth.searchResult.searchDataClass.AllProductDataClass
import com.example.augmanium.afterAuth.searchResult.searchDataClass.CatagoryDataClass
import com.example.augmanium.databinding.ActivitySearchResultBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(): ViewModel(){

    var productsTitleArrayList: ArrayList<CatagoryDataClass> = ArrayList()
    val allProductArrayList: ArrayList<AllProductDataClass> = ArrayList()

    fun screenBinding(activityContext : Context, binding: ActivitySearchResultBinding){
        productsTitleArrayList.clear()
        allProductArrayList.clear()

        productTitleRv(binding)
        allProductRv(binding, activityContext)


    }

    private fun allProductRv(binding: ActivitySearchResultBinding, activityContext: Context) {
        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))
        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))
        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))
        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))
        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))

        binding.allProductRV.also {
            it.adapter = AllProductViewAdapter(allProductArrayList)
            it.setHasFixedSize(true)
        }
    }

    fun productTitleRv(binding : ActivitySearchResultBinding){
        productsTitleArrayList.add(CatagoryDataClass("All"))
        productsTitleArrayList.add(CatagoryDataClass("Women"))
        productsTitleArrayList.add(CatagoryDataClass("Men"))
        productsTitleArrayList.add(CatagoryDataClass("Best Sellers"))

        binding.categoryRv.also {
            it.adapter = CatagoryAdapter(productsTitleArrayList)
            it.setHasFixedSize(true)
        }
    }
}