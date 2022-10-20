package com.example.augmanium.afterAuth.category.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.augmanium.ProductDetailsActivity
import com.example.augmanium.afterAuth.category.CategoryScreen
import com.example.augmanium.afterAuth.category.SpecificCategoryProducts
import com.example.augmanium.afterAuth.mainActivity.dataClass.ProductDetailCategoryProductDataClass
import com.example.augmanium.afterAuth.mainActivity.dataClass.ReviewDataclass
import com.example.augmanium.afterAuth.searchscreen.SearchActivity
import com.example.augmanium.databinding.ActivityCategoryScreenBinding
import com.example.augmanium.databinding.ActivityProductDetailsBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryScreenViewModel @Inject constructor(): ViewModel() {

    lateinit var tinyDB: TinyDB
    lateinit var activityBinding: ActivityCategoryScreenBinding
    lateinit var activityContext: Context
    var productCount = 1
    val categoryProductArrayList: ArrayList<ProductDetailCategoryProductDataClass> = ArrayList()
    val productReviewArrayList: ArrayList<ReviewDataclass> = ArrayList()
    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference
    var countElectronics = 0
    var countChildren = 0
    var countMen = 0
    var countWomen = 0
    var countAll = 0
    var countBestSeller = 0

    fun categoryScreenItems(binding: ActivityCategoryScreenBinding, context: Context){

        activityBinding = binding
        activityContext = context
        tinyDB = TinyDB(context)
//        var data = tinyDB.getObject(K.PRODUCT_DATA, AllProductDataClass::class.java)

        getData("Best Sellers")
        getData("Children")
        getData("Men")
        getData("women")
        getData("Electronics")
        getData("All")


        binding.apply {
            kids.setOnClickListener {
                val intent = Intent(activityContext,SpecificCategoryProducts::class.java)
                tinyDB.putString(K.INTENT_CATEGORY,"Children")
                activityContext.startActivity(intent)
            }
            women.setOnClickListener {
                val intent = Intent(activityContext,SpecificCategoryProducts::class.java)
                tinyDB.putString(K.INTENT_CATEGORY,"women")
                activityContext.startActivity(intent)
            }
            men.setOnClickListener {
                val intent = Intent(activityContext,SpecificCategoryProducts::class.java)
                tinyDB.putString(K.INTENT_CATEGORY,"Men")
                activityContext.startActivity(intent)
            }
            electronic.setOnClickListener {
                val intent = Intent(activityContext,SpecificCategoryProducts::class.java)
                tinyDB.putString(K.INTENT_CATEGORY,"Electronics")
                activityContext.startActivity(intent)
            }
            searchBtn.setOnClickListener {
                val intent = Intent(activityContext,SearchActivity::class.java)
                activityContext.startActivity(intent)
            }
            backBtn.setOnClickListener {
                (activityContext as CategoryScreen).finish()
            }



        }

    }

    fun setCount(){
//        categoryProductArrayList.clear()
        activityBinding.electronicCount.text = countElectronics.toString()
        activityBinding.womenCount.text = countWomen.toString()
        activityBinding.menCount.text = countMen.toString()
        activityBinding.kidsCount.text = countChildren.toString()
        tinyDB.putString(K.ALL_COUNT,countAll.toString())
        tinyDB.putString(K.CHILDREN_COUNT,countChildren.toString())
        tinyDB.putString(K.MEN_COUNT,countMen.toString())
        tinyDB.putString(K.WOMEN_COUNT,countWomen.toString())
        tinyDB.putString(K.ELECTRONICS_COUNT,countElectronics.toString())
        tinyDB.putString(K.BESTSALLER_COUNT,countBestSeller.toString())

    }

    fun getData(category: String){
        categoryProductArrayList.clear()
        database.child("Product").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {

                    Log.d("NODE___"," ${snap.key} $snapshot")
//                    for (products in snap.children) {
                    Log.d("NODE___"," ${snap.key} $snap")
                    val product = snap.getValue(ProductDetailCategoryProductDataClass::class.java)
                    when (category) {

                        "All" -> {


                                 countAll = countAll + 1
                                setCount()
                        }
                        "women" -> {
                            if(product!!.productCategory == category){
                                countWomen = countWomen + 1
                                Log.d(
                                    "PRODUCT_DISPLAY $category",
                                    "$countWomen"
                                )
                                setCount()
                            }
                            else {
                                Log.d("PRODUCT_DISPLAY $category", "NOTHING TO SHOW___!!!!!!")
                            }

                        }
                        "Men" -> {
                            if(product!!.productCategory == category){
                                countMen = countMen + 1
                                Log.d(
                                    "PRODUCT_DISPLAY $category",
                                    "$countMen"
                                )
                                setCount()
                            }
                            else {
                                Log.d("PRODUCT_DISPLAY $category", "NOTHING TO SHOW___!!!!!!")
                            }
                        }
                        "Children" -> {
                            if(product!!.productCategory == category){
                                countChildren = countChildren + 1
                                Log.d(
                                    "PRODUCT_DISPLAY $category",
                                    "$countChildren"
                                )
                                setCount()
                            }
                            else {
                                Log.d("PRODUCT_DISPLAY $category", "NOTHING TO SHOW___!!!!!!")
                            }
                        }
                        "Electronics" -> {
                            if(product!!.productCategory == category){
                                countElectronics = countElectronics + 1
                                Log.d(
                                    "PRODUCT_DISPLAY $category",
                                    "$countElectronics"
                                )
                                setCount()
                            }
                            else {
                                Log.d("PRODUCT_DISPLAY $category", "NOTHING TO SHOW___!!!!!!")
                            }
                        }
                        "Best Sellers" -> {
                            if(product!!.productCategory == category){
                                countBestSeller = countBestSeller + 1
                                Log.d(
                                    "PRODUCT_DISPLAY $category",
                                    "$countBestSeller"
                                )
                                setCount()
                            }
                            else {
                                Log.d("PRODUCT_DISPLAY $category", "NOTHING TO SHOW___!!!!!!")
                            }
                        }


                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")
            }

        })


    }

}