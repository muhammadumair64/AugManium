package com.example.augmanium.afterAuth.category.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.augmanium.afterAuth.category.SpecificCategoryProducts
import com.example.augmanium.afterAuth.category.adapter.AllCategoriesAdapter
import com.example.augmanium.afterAuth.category.adapter.SpecificCategoryProductAdapter
import com.example.augmanium.afterAuth.category.dataClass.AllCategoryDataClass
import com.example.augmanium.afterAuth.category.dataClass.SpecificCatagoryProductDataClass
import com.example.augmanium.afterAuth.interfaces.OnItemClickListener
import com.example.augmanium.afterAuth.searchscreen.SearchActivity
import com.example.augmanium.databinding.ActivityAllCategoriesBinding
import com.example.augmanium.databinding.ActivitySpecificCategoryProductsBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllCategoriesViewModel @Inject constructor(): ViewModel(), OnItemClickListener{

    lateinit var activityBinding: ActivityAllCategoriesBinding
    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference
    lateinit var activityContext: Context
    lateinit var tinyDB: TinyDB
    val allCategoriesArrayList: ArrayList<AllCategoryDataClass> = ArrayList()

    fun allCategoryRv(binding: ActivityAllCategoriesBinding, context: Context) {
        activityBinding = binding
        activityContext = context
        tinyDB = TinyDB(context)


       var allCount = tinyDB.getString(K.ALL_COUNT)
       var childrenCount = tinyDB.getString(K.CHILDREN_COUNT)
       var menCount = tinyDB.getString(K.MEN_COUNT)
       var womenCoumt = tinyDB.getString(K.WOMEN_COUNT)
       var electronicCount = tinyDB.getString(K.ELECTRONICS_COUNT)
       var bestSellerCount = tinyDB.getString(K.BESTSALLER_COUNT)


        binding.searchBtn.setOnClickListener {
            val intent = Intent(activityContext, SearchActivity::class.java)
            activityContext.startActivity(intent)

        }

//        getData()

        allCategoriesArrayList.add(AllCategoryDataClass("All", allCount))
        allCategoriesArrayList.add(AllCategoryDataClass("women", womenCoumt))
        allCategoriesArrayList.add(AllCategoryDataClass("Men", menCount))
        allCategoriesArrayList.add(AllCategoryDataClass("Kids", childrenCount))
        allCategoriesArrayList.add(AllCategoryDataClass("Electronics", electronicCount))
        allCategoriesArrayList.add(AllCategoryDataClass("Best Seller", bestSellerCount))

        binding.allCategoryRv.also {
            it.adapter = AllCategoriesAdapter(allCategoriesArrayList, this)
            it.setHasFixedSize(true)
        }
    }


    fun getData(){

        allCategoriesArrayList.clear()
        activityBinding.allCategoryRv.adapter = null
        database.child("Product").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.getChildren()) {
                    Log.d("NODE___"," ${snap.key} $snapshot")
//                    for (products in snap.children) {
                    Log.d("NODE___"," ${snap.key} $snap")
                    var product = snap.getValue(AllCategoryDataClass::class.java)
                    rvData(product!!)



                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")
            }

        })

    }
    fun rvData(product: AllCategoryDataClass){

            allCategoriesArrayList.add(product!!)

            activityBinding.allCategoryRv.also {
                it.adapter = AllCategoriesAdapter(allCategoriesArrayList, this)
                it.setHasFixedSize(true)
            }



    }

    override fun onClick(position: Int) {

    }

    override fun moveToNextScreen(position: Int) {
        var category = allCategoriesArrayList[position].productCategory
        val intent = Intent(activityContext, SpecificCategoryProducts::class.java)
        tinyDB.putString(K.INTENT_CATEGORY,category)
        activityContext.startActivity(intent)
    }


}