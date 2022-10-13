package com.example.augmanium.afterAuth.mainActivity.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.augmanium.afterAuth.mainActivity.Adapter.AllProductAdapter
import com.example.augmanium.afterAuth.mainActivity.Adapter.CatagoryAdapter
import com.example.augmanium.afterAuth.searchResult.searchDataClass.AllProductDataClass
import com.example.augmanium.afterAuth.searchResult.searchDataClass.CatagoryDataClass
import com.example.augmanium.databinding.FragmentHomeBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): ViewModel(){

    var productsTitleArrayList: ArrayList<CatagoryDataClass> = ArrayList()
    val allProductArrayList: ArrayList<AllProductDataClass> = ArrayList()

    fun catagoryRvBinding(activityContext: Context, binding: FragmentHomeBinding){
        productsTitleArrayList.clear()
        allProductArrayList.clear()
        productTitleRv(binding)
        allProductRv(binding, activityContext)


    }

    private fun allProductRv(binding:FragmentHomeBinding , activityContext: Context) {
        allProductArrayList.add(AllProductDataClass("T-Shirt", "Men", "$15"))
        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))
        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))
        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))
        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))

        binding.allProductRV.also {
            it.adapter = AllProductAdapter(allProductArrayList)

            it.layoutManager = GridLayoutManager(activityContext,2)
            it.setHasFixedSize(true)



        }
    }

    fun productTitleRv(binding : FragmentHomeBinding){
        productsTitleArrayList.add(CatagoryDataClass("All"))
        productsTitleArrayList.add(CatagoryDataClass("Women"))
        productsTitleArrayList.add(CatagoryDataClass("Men"))
        productsTitleArrayList.add(CatagoryDataClass("Children"))
        productsTitleArrayList.add(CatagoryDataClass("Electronics"))
        productsTitleArrayList.add(CatagoryDataClass("Best Sellers"))

        binding.categoryRv.also {
            it.adapter = CatagoryAdapter(productsTitleArrayList)
            it.setHasFixedSize(true)

        }
    }
}