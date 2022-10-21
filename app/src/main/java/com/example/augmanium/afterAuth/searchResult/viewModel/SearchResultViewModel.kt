package com.example.augmanium.afterAuth.mainActivity.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.augmanium.afterAuth.productDetails.ProductDetailsActivity
import com.example.augmanium.afterAuth.interfaces.OnItemClickListener
import com.example.augmanium.afterAuth.mainActivity.Adapter.AllProductViewAdapter
import com.example.augmanium.afterAuth.search.searchDataClass.SearchAllProductDataClass
import com.example.augmanium.afterAuth.search.searchDataClass.SearchCatagoryDataClass
import com.example.augmanium.afterAuth.searchResult.mainClass.SearchResult
import com.example.augmanium.databinding.ActivitySearchResultBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(): ViewModel(), OnItemClickListener {

    lateinit var tinyDB: TinyDB
    lateinit var activityBinding: ActivitySearchResultBinding
    lateinit var context: Context
    var productsTitleArrayList: ArrayList<SearchCatagoryDataClass> = ArrayList()
    val allProductArrayList: ArrayList<SearchAllProductDataClass> = ArrayList()
    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference


    fun screenBinding(activityContext : Context, binding: ActivitySearchResultBinding){

        activityBinding = binding
        context = activityContext
        tinyDB = TinyDB(context)

        binding.searchBtn.setOnClickListener {
            (context as SearchResult).finish()
        }

        var search_for= tinyDB.getString(K.SEARCH_QUERY)
        productsTitleArrayList.clear()
        allProductArrayList.clear()

        getData(search_for!!)


    }


    fun getData(productName: String){

        allProductArrayList.clear()
        activityBinding.progressLayout.visibility= View.VISIBLE
        activityBinding.allProductRV.adapter = null
        database.child("Product").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.getChildren()) {
                    Log.d("NODE___"," ${snap.key} $snapshot")
//                    for (products in snap.children) {
                    Log.d("NODE___"," ${snap.key} $snap")
                    var product = snap.getValue(SearchAllProductDataClass::class.java)
                    var nameProduct = product!!.productName
                    if (productName == nameProduct.toString()){
                        Log.d("PRONME",productName)
                        allProductArrayList.add(product)
                        rv()
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")
            }

        })

    }
    fun rv(){

        activityBinding.progressLayout.visibility=View.INVISIBLE
        activityBinding.allProductRV.also {
            it.adapter = AllProductViewAdapter(allProductArrayList, this)
            it.setHasFixedSize(true)
        }
    }


    fun onnextscreen(productData: SearchAllProductDataClass) {

        var intent = Intent(context, ProductDetailsActivity::class.java)

        tinyDB.putObject(K.PRODUCT_DATA, productData)
        tinyDB.putString(K.PRODUCT_ID,productData.id)
        Log.d("PRODUCT_ID","${productData.id}")
        (context ).startActivity(intent)
    }

    override fun onClick(position: Int) {

    }

    override fun moveToNextScreen(position: Int) {
        val order = allProductArrayList[position]
        onnextscreen(order)
            }
}