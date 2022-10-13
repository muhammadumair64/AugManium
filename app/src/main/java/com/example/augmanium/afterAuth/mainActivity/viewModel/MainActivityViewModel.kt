package com.example.augmanium.afterAuth.mainActivity.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.augmanium.ProductDetailsActivity
import com.example.augmanium.afterAuth.interfaces.OnItemClickListener
import com.example.augmanium.afterAuth.mainActivity.Adapter.AllProductAdapter
import com.example.augmanium.afterAuth.mainActivity.Adapter.CatagoryAdapter
import com.example.augmanium.afterAuth.mainActivity.dataClass.AllProductDataClass
import com.example.augmanium.afterAuth.mainActivity.dataClass.CatagoryDataClass
import com.example.augmanium.databinding.FragmentHomeBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): ViewModel(), OnItemClickListener{

    lateinit var tinyDB: TinyDB
    lateinit var activityContextGlobal: Context
    lateinit var activityBinding: FragmentHomeBinding
    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference

    var productsTitleArrayList: ArrayList<CatagoryDataClass> = ArrayList()
    val allProductArrayList: ArrayList<AllProductDataClass> = ArrayList()

    fun catagoryRvBinding(activityContext: Context, binding: FragmentHomeBinding){
        productsTitleArrayList.clear()
        allProductArrayList.clear()
        productTitleRv(binding)
        activityContextGlobal = activityContext
        activityBinding = binding
        tinyDB = TinyDB(activityContext)
        getData(0)



    }

    @SuppressLint("NotifyDataSetChanged")
    private fun allProductRv() {
//        allProductArrayList.add(AllProductDataClass("T-Shirt", "Men", "$15"))
//        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))
//        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))
//        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))
//        allProductArrayList.add(AllProductDataClass("White Top", "Women", "$15"))
        Log.d("ARRAYLIST_DATA","$allProductArrayList")
            activityBinding.allProductRV.also {
            it.adapter = AllProductAdapter(allProductArrayList, this)
            it.layoutManager = GridLayoutManager(activityContextGlobal,2)
            it.setHasFixedSize(true)
            it.adapter!!.notifyDataSetChanged()
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
            it.adapter = CatagoryAdapter(productsTitleArrayList, this)
            it.setHasFixedSize(true)

        }


    }

    override fun onClick(position: Int) {
        //position 0 = all
        //position 1 = Women
        //position 2 = Men
        //position 3 = Children
        //position 4 = Electronics
        //position 5 = Best Seller

        getData(position)


        Log.d("ITEM_CLICKED","POSITION $position")
    }

    override fun moveToNextScreen(position: Int) {
        val order = allProductArrayList[position]
        onnextscreen(order)
    }
    fun onnextscreen(productData: AllProductDataClass) {

        var intent = Intent(activityContextGlobal, ProductDetailsActivity::class.java)

        tinyDB.putObject(K.PRODUCT_DATA, productData)
        (activityContextGlobal ).startActivity(intent)
    }


    fun getData(category: Int){

        Log.d("category___"," $category")

        allProductArrayList.clear()
        activityBinding.allProductRV.adapter = null
        database.child("Product").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.getChildren()) {
                    Log.d("NODE___"," ${snap.key} $snapshot")
//                    for (products in snap.children) {
                        Log.d("NODE___"," ${snap.key} $snap")
                        var product = snap.getValue(AllProductDataClass::class.java)

                        when (category) {
                            0 -> {
                                    allProductArrayList.add(product!!)
                                    tinyDB.putListObject(K.SPECIFIC_CATEGORY_PRODUCTS,allProductArrayList)
                                allProductRv()
                                    Log.d(
                                        "ELECTRONICS_PRODUCTS",
                                        "$allProductArrayList ${snap.key}"
                                    )

                            }
                            1 -> {
                                if (product!!.productCategory == "women"){
                                    allProductArrayList.add(product!!)
                                    allProductRv()
                                    Log.d(
                                        "Women_PRODUCTS",
                                        "$allProductArrayList ${snap.key}"
                                    )
                                }
                                else {
                                    Log.d("Women_PRODUCT", "NOTHING TO SHOW___!!!!!!")
                                }
                            }
                            2 -> {
                                if (product!!.productCategory == "Men"){
                                    allProductArrayList.add(product!!)
                                    allProductRv()
                                    Log.d(
                                        "Men_PRODUCTS",
                                        "$allProductArrayList ${snap.key}"
                                    )
                                }
                                else {
                                    Log.d("Men_PRODUCT", "NOTHING TO SHOW___!!!!!!")
                                }
                            }
                            3 -> {
                                if (product!!.productCategory == "Children"){
                                    allProductArrayList.add(product!!)
                                    allProductRv()
                                    Log.d(
                                        "Children_PRODUCTS",
                                        "$allProductArrayList ${snap.key}"
                                    )
                                }
                                else {
                                    Log.d("Children_PRODUCT", "NOTHING TO SHOW___!!!!!!")
                                }
                            }
                            4 -> {
                                if (product!!.productCategory == "Electronics"){
                                    allProductArrayList.add(product!!)
                                    tinyDB.putListObject(K.SPECIFIC_CATEGORY_PRODUCTS,allProductArrayList)
                                    allProductRv()
                                    Log.d(
                                        "ELECTRONICS_PRODUCTS",
                                        "$allProductArrayList ${snap.key}"
                                    )
                                }
                                else {
                                    Log.d("ELECTRONICS_PRODUCT", "NOTHING TO SHOW___!!!!!!")
                                }


                            }
                            5 -> {
                                if (product!!.productCategory == "Best Seller"){
                                    allProductArrayList.add(product!!)
                                    allProductRv()
                                    Log.d(
                                        "Best_Seller_PRODUCTS",
                                        "$allProductArrayList ${snap.key}"
                                    )
                                }
                                else {
                                    Log.d("Best_Seller_PRODUCT", "NOTHING TO SHOW___!!!!!!")
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