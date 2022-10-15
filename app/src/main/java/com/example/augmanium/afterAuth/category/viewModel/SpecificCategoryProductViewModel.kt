package com.example.augmanium.afterAuth.category.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.augmanium.ProductDetailsActivity
import com.example.augmanium.afterAuth.category.AllCategories
import com.example.augmanium.afterAuth.category.adapter.SpecificCategoryProductAdapter
import com.example.augmanium.afterAuth.category.dataClass.SpecificCatagoryProductDataClass
import com.example.augmanium.afterAuth.interfaces.OnItemClickListener
import com.example.augmanium.afterAuth.mainActivity.Adapter.ProductDetailAdapter
import com.example.augmanium.afterAuth.mainActivity.dataClass.AllProductDataClass
import com.example.augmanium.afterAuth.mainActivity.dataClass.ProductDetailCategoryProductDataClass
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
class SpecificCategoryProductViewModel @Inject constructor(): ViewModel(), OnItemClickListener{

    val specificCategoryProductArrayList: ArrayList<SpecificCatagoryProductDataClass> = ArrayList()
    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference
    lateinit var activityContext: Context
    lateinit var tinyDB: TinyDB


    fun allProductRv(binding: ActivitySpecificCategoryProductsBinding, context: Context) {

        activityContext = context
        tinyDB = TinyDB(context)
        var category = tinyDB.getString(K.INTENT_CATEGORY)
        binding.categoryName.text = category
        getData(category!!,binding)
        binding.categoryView.setOnClickListener {
            var intent = Intent(activityContext, AllCategories::class.java)
            (activityContext ).startActivity(intent)
        }

//        specificCategoryProductArrayList.add(SpecificCatagoryProductDataClass("White Top"))
//        specificCategoryProductArrayList.add(SpecificCatagoryProductDataClass("White Top"))
//        specificCategoryProductArrayList.add(SpecificCatagoryProductDataClass("White Top"))
//        specificCategoryProductArrayList.add(SpecificCatagoryProductDataClass("White Top"))
//        specificCategoryProductArrayList.add(SpecificCatagoryProductDataClass("White Top"))
//
//        binding.allProductRV.also {
//            it.adapter = SpecificCategoryProductAdapter(specificCategoryProductArrayList)
//            it.setLayoutManager(GridLayoutManager(activityContext,2))
//            it.setHasFixedSize(true)
//        }
    }

    fun rvData(product: SpecificCatagoryProductDataClass,binding: ActivitySpecificCategoryProductsBinding, category: String){
        if(product!!.productCategory == category){
            specificCategoryProductArrayList.add(product!!)

            binding.allProductRV.also {
                it.adapter = SpecificCategoryProductAdapter(specificCategoryProductArrayList, this)
                it.setLayoutManager(GridLayoutManager(activityContext,2))
                it.setHasFixedSize(true)
                it.adapter!!.notifyDataSetChanged()
            }

            Log.d(
                "PRODUCT_DISPLAY $category",
                "$specificCategoryProductArrayList"
            )
        }
        else {
            Log.d("PRODUCT_DISPLAY $category", "NOTHING TO SHOW___!!!!!!")
        }
    }


    fun getData(category: String, binding: ActivitySpecificCategoryProductsBinding){

        specificCategoryProductArrayList.clear()
        binding.allProductRV.adapter = null
        database.child("Product").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.getChildren()) {
                    Log.d("NODE___"," ${snap.key} $snapshot")
//                    for (products in snap.children) {
                    Log.d("NODE___"," ${snap.key} $snap")
                    var product = snap.getValue(SpecificCatagoryProductDataClass::class.java)
                    when (category) {
                        "All" -> {

                            rvData(product!!,binding,"All")
                        }
                        "women" -> {
                            rvData(product!!,binding,"women")

                        }
                        "Men" -> {
                            rvData(product!!,binding,"Men")
                        }
                        "Children" -> {
                            rvData(product!!,binding,"Children")
                        }
                        "Electronics" -> {
                            rvData(product!!,binding,"Electronics")
                        }
                        "Best Seller" -> {
                            rvData(product!!,binding,"Best Seller")
                        }


                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")
            }

        })

    }
    fun onnextscreen(productData: SpecificCatagoryProductDataClass) {

        var intent = Intent(activityContext, ProductDetailsActivity::class.java)

        tinyDB.putObject(K.PRODUCT_DATA, productData)
        (activityContext ).startActivity(intent)
    }

    override fun onClick(position: Int) {

    }

    override fun moveToNextScreen(position: Int) {
        val order = specificCategoryProductArrayList[position]
        onnextscreen(order)
    }
}