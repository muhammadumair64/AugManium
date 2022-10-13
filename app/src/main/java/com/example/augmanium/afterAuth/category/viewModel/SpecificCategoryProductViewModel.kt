package com.example.augmanium.afterAuth.category.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.augmanium.afterAuth.category.adapter.SpecificCategoryProductAdapter
import com.example.augmanium.afterAuth.category.dataClass.SpecificCatagoryProductDataClass
import com.example.augmanium.databinding.ActivitySpecificCategoryProductsBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpecificCategoryProductViewModel @Inject constructor(): ViewModel(){

    val specificCategoryProductArrayList: ArrayList<SpecificCatagoryProductDataClass> = ArrayList()

    fun allProductRv(binding: ActivitySpecificCategoryProductsBinding, activityContext: Context) {
        specificCategoryProductArrayList.add(SpecificCatagoryProductDataClass("White Top"))
        specificCategoryProductArrayList.add(SpecificCatagoryProductDataClass("White Top"))
        specificCategoryProductArrayList.add(SpecificCatagoryProductDataClass("White Top"))
        specificCategoryProductArrayList.add(SpecificCatagoryProductDataClass("White Top"))
        specificCategoryProductArrayList.add(SpecificCatagoryProductDataClass("White Top"))

        binding.allProductRV.also {
            it.adapter = SpecificCategoryProductAdapter(specificCategoryProductArrayList)
            it.setLayoutManager(GridLayoutManager(activityContext,2))
            it.setHasFixedSize(true)
        }
    }
}