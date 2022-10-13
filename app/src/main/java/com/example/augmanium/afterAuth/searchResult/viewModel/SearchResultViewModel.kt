package com.example.augmanium.afterAuth.mainActivity.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.augmanium.afterAuth.mainActivity.Adapter.AllProductViewAdapter
import com.example.augmanium.afterAuth.mainActivity.Adapter.CatagoryAdapter
import com.example.augmanium.afterAuth.searchResult.searchDataClass.AllProductDataClass
import com.example.augmanium.afterAuth.searchResult.searchDataClass.CatagoryDataClass
import com.example.augmanium.afterAuth.search.searchDataClass.SearchAllProductDataClass
import com.example.augmanium.afterAuth.search.searchDataClass.SearchCatagoryDataClass
import com.example.augmanium.databinding.ActivitySearchResultBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(): ViewModel(){

    var productsTitleArrayList: ArrayList<SearchCatagoryDataClass> = ArrayList()
    val allProductArrayList: ArrayList<SearchAllProductDataClass> = ArrayList()

    fun screenBinding(activityContext : Context, binding: ActivitySearchResultBinding){
        productsTitleArrayList.clear()
        allProductArrayList.clear()

        productTitleRv(binding)
        allProductRv(binding, activityContext)


    }

    private fun allProductRv(binding: ActivitySearchResultBinding, activityContext: Context) {
        allProductArrayList.add(SearchAllProductDataClass("White Top", "Women", "$15"))
        allProductArrayList.add(SearchAllProductDataClass("White Top", "Women", "$15"))
        allProductArrayList.add(SearchAllProductDataClass("White Top", "Women", "$15"))
        allProductArrayList.add(SearchAllProductDataClass("White Top", "Women", "$15"))
        allProductArrayList.add(SearchAllProductDataClass("White Top", "Women", "$15"))

        binding.allProductRV.also {
            it.adapter = AllProductViewAdapter(allProductArrayList)
            it.setHasFixedSize(true)
        }
    }

    fun productTitleRv(binding : ActivitySearchResultBinding){
        productsTitleArrayList.add(SearchCatagoryDataClass("All"))
        productsTitleArrayList.add(SearchCatagoryDataClass("Women"))
        productsTitleArrayList.add(SearchCatagoryDataClass("Men"))
        productsTitleArrayList.add(SearchCatagoryDataClass("Best Sellers"))

//        binding.categoryRv.also {
//            it.adapter = CatagoryAdapter(productsTitleArrayList)
//            it.setHasFixedSize(true)
//        }
    }
}