package com.example.augmanium.afterAuth.category.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.augmanium.afterAuth.category.adapter.AllCategoriesAdapter
import com.example.augmanium.afterAuth.category.adapter.SpecificCategoryProductAdapter
import com.example.augmanium.afterAuth.category.dataClass.AllCategoryDataClass
import com.example.augmanium.afterAuth.category.dataClass.SpecificCatagoryProductDataClass
import com.example.augmanium.databinding.ActivityAllCategoriesBinding
import com.example.augmanium.databinding.ActivitySpecificCategoryProductsBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllCategoriesViewModel @Inject constructor(): ViewModel(){

    val allCategoriesArrayList: ArrayList<AllCategoryDataClass> = ArrayList()

    fun allCategoryRv(binding: ActivityAllCategoriesBinding, activityContext: Context) {
        allCategoriesArrayList.add(AllCategoryDataClass("Collection", "160 Items"))
        allCategoriesArrayList.add(AllCategoryDataClass("Women", "160 Items"))
        allCategoriesArrayList.add(AllCategoryDataClass("Men", "160 Items"))
        allCategoriesArrayList.add(AllCategoryDataClass("Kids", "160 Items"))
        allCategoriesArrayList.add(AllCategoryDataClass("Best Seller", "160 Items"))

        binding.allCategoryRv.also {
            it.adapter = AllCategoriesAdapter(allCategoriesArrayList)
            it.setHasFixedSize(true)
        }
    }

}