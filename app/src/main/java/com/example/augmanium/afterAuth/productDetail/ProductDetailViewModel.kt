package com.example.augmanium.afterAuth.productDetail

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.augmanium.ProductDetailsActivity
import com.example.augmanium.afterAuth.interfaces.OnItemClickListener

import com.example.augmanium.afterAuth.mainActivity.Adapter.ProductDetailAdapter
import com.example.augmanium.afterAuth.mainActivity.Adapter.ProductReviewAapter
import com.example.augmanium.afterAuth.mainActivity.dataClass.AllProductDataClass
import com.example.augmanium.afterAuth.mainActivity.dataClass.CartDataClass
import com.example.augmanium.afterAuth.mainActivity.dataClass.ProductDetailCategoryProductDataClass
import com.example.augmanium.afterAuth.mainActivity.dataClass.ReviewDataclass
import com.example.augmanium.afterAuth.search.searchDataClass.SearchAllProductDataClass
import com.example.augmanium.afterAuth.wrightReview.WriteReviewScreen
import com.example.augmanium.databinding.ActivityProductDetailsBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.activity_product_details.view.*
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(): ViewModel(), OnItemClickListener {
    lateinit var tinyDB: TinyDB
    lateinit var activityBinding: ActivityProductDetailsBinding
    lateinit var activityContext: Context
    var productCount = 1
    val categoryProductArrayList: ArrayList<ProductDetailCategoryProductDataClass> = ArrayList()
    val productReviewArrayList: ArrayList<ReviewDataclass> = ArrayList()
    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference

    fun detailScreenItems(binding: ActivityProductDetailsBinding, context: Context){

        activityBinding = binding
        activityContext = context
        tinyDB = TinyDB(context)
        var data = tinyDB.getObject(K.PRODUCT_DATA, AllProductDataClass::class.java)
        var productId = tinyDB.getString(K.PRODUCT_ID)

        binding.apply {

            writeRevirw.setOnClickListener {
                var  intent = Intent(activityContext, WriteReviewScreen::class.java)
                activityContext.startActivity(intent)
            }
            productCount = count.text.trim().toString().toInt()
            decrease.setOnClickListener {

                if (productCount > 1){
                    var updatedCount = --productCount
                    count.text = updatedCount.toString()
                    Log.d("productCount-1","$productCount $updatedCount")

                }
                Log.d("productCount","$productCount")
            }
            increase.setOnClickListener {

                var updatedCount = ++productCount
                count.text = updatedCount.toString()
                Log.d("productCount+1","$productCount $updatedCount")
            }
            cartBtn.setOnClickListener {
                addToCart(data)
            }
            backButton.setOnClickListener {
                (activityContext as ProductDetailsActivity).finish()
            }

            categoryTitle.text = data.productCategory
            description.text = data.productDescription
            Size.text = data.productSize
            productName.text = data.productName
            productPrice.text = data.prize

            Log.d("I_________","${data.id}")


            Glide.with(context)
                .load(data.image)
                .override(300, 200)
                .into(binding.image);
            allProductRV.also {
                it.adapter = ProductDetailAdapter(categoryProductArrayList, this@ProductDetailViewModel)
                it.setHasFixedSize(true)
            }

        }

        getData(data.productCategory!!)
        getReview(productId!!)

    }

    fun rvData(product: ProductDetailCategoryProductDataClass, category: String){
        if(product!!.productCategory == category){
            categoryProductArrayList.add(product!!)

            activityBinding.allProductRV.also {
                it.adapter = ProductDetailAdapter(categoryProductArrayList,this)
                it.setHasFixedSize(true)
                it.adapter!!.notifyDataSetChanged()
            }

            Log.d(
                "PRODUCT_DISPLAY $category",
                "$categoryProductArrayList"
            )
        }
        else {
            Log.d("PRODUCT_DISPLAY $category", "NOTHING TO SHOW___!!!!!!")
        }
    }



    fun getData(category: String){

        categoryProductArrayList.clear()
        activityBinding.allProductRV.adapter = null
        database.child("Product").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.getChildren()) {
                    Log.d("NODE___"," ${snap.key} $snapshot")
//                    for (products in snap.children) {
                    Log.d("NODE___"," ${snap.key} $snap")
                    var product = snap.getValue(ProductDetailCategoryProductDataClass::class.java)
                    when (category) {
                        "All" -> {

                            rvData(product!!,"All")
                        }
                        "women" -> {
                            rvData(product!!,"women")

                        }
                        "Men" -> {
                            rvData(product!!,"Men")
                        }
                        "Children" -> {
                            rvData(product!!,"Children")
                        }
                        "Electronics" -> {
                            rvData(product!!,"Electronics")
                        }
                        "Best Sellers" -> {
                            rvData(product!!,"Best Sellers")
                        }


                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")
            }

        })

    }

    fun getReview(productId: String){
        Log.d("GET_REVIEW","${productId} ")

        database.child("Product").child(productId).child("review").addListenerForSingleValueEvent(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.getChildren()) {
                    Log.d("REVIEWE_KEY","${snap.key} ${snap.value}")
                    val review = snap.getValue(ReviewDataclass::class.java)
                    productReviewArrayList.add(review!!)
                    activityBinding.reviewRV.also {
                        it.adapter = ProductReviewAapter(productReviewArrayList)
                        it.setHasFixedSize(true)
                        it.adapter!!.notifyDataSetChanged()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")
            }

        })
    }


    fun addToCart(data: AllProductDataClass){

        val email = tinyDB.getString(K.EMAIL)
        val separated: List<String> = email!!.split("@")
        val nodeName = separated[0]

        Log.d("NODE_NAME_USER","$nodeName")
        val cartItem = CartDataClass(data.image,data.prize,data.productCategory,data.productColor,data.productDescription,data.productName,data.productSize,data.id,productCount.toString())
        val rootRef = FirebaseDatabase.getInstance().reference
        val yourRef = rootRef.child("Cart").child(nodeName).child(data.productName!!)
        yourRef.setValue(cartItem)
    }

    fun onnextscreen(productData: ProductDetailCategoryProductDataClass) {

        var intent = Intent(activityContext, ProductDetailsActivity::class.java)

        tinyDB.putObject(K.PRODUCT_DATA, productData)
        tinyDB.putString(K.PRODUCT_ID,productData.id)
        Log.d("PRODUCT_ID","${productData.id}")
        (activityContext ).startActivity(intent)
        (activityContext as ProductDetailsActivity).finish()
    }

    override fun onClick(position: Int) {

    }

    override fun moveToNextScreen(position: Int) {
        val order = categoryProductArrayList[position]
        onnextscreen(order)
    }


}