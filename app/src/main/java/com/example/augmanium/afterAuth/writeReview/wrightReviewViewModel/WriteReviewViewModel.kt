package com.example.augmanium.afterAuth.writeReview.wrightReviewViewModel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.augmanium.afterAuth.mainActivity.dataClass.AllProductDataClass
import com.example.augmanium.afterAuth.writeReview.WriteReviewScreen
import com.example.augmanium.afterAuth.writeReview.adapter.WriteReviewAdapter
import com.example.augmanium.afterAuth.writeReview.dataClass.UploadReview
import com.example.augmanium.afterAuth.writeReview.dataClass.WriteReviewDataClass
import com.example.augmanium.databinding.ActivityWriteReviewScreenBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WriteReviewViewModel @Inject constructor() :ViewModel() {

    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference
    val productReviewArrayList: ArrayList<WriteReviewDataClass> = ArrayList()
    var productIdGlobal = ""
    lateinit var activityBinding: ActivityWriteReviewScreenBinding
    lateinit var context: Context
    lateinit var tinyDB: TinyDB

    fun viewBinding(
        binding: ActivityWriteReviewScreenBinding,
        writeReviewScreen: Context
    ) {
        activityBinding = binding
        context = writeReviewScreen
        tinyDB = TinyDB(context)
        getReview(tinyDB.getString(K.PRODUCT_ID)!!)
        var data = tinyDB.getObject(K.PRODUCT_DATA, AllProductDataClass::class.java)

        activityBinding.apply {
            productPrice.text = data.prize
            productName.text = data.productName
            detail.setOnClickListener {
                (context as WriteReviewScreen).finish()
            }

            submit.setOnClickListener {
                val review = editReview.text.toString().trim()
                uploadReview(review)
            }
            backButton.setOnClickListener {
                (context as WriteReviewScreen).finish()
            }
            detail.setOnClickListener {
                (context as WriteReviewScreen).finish()
            }



        }







    }

    fun getReview(productId: String){
        activityBinding.progressLayout.visibility= View.VISIBLE
        Log.d("GET_REVIEW","${productId} ")
        productIdGlobal = productId
        database.child("Product").child(productId).child("review").addListenerForSingleValueEvent(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.getChildren()) {
                    Log.d("REVIEWE_KEY","${snap.key} ${snap.value}")
                    val review = snap.getValue(WriteReviewDataClass::class.java)
                    productReviewArrayList.add(review!!)
                    activityBinding.progressLayout.visibility=View.INVISIBLE
                    activityBinding.review.also {
                        it.adapter = WriteReviewAdapter(productReviewArrayList)
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

    fun uploadReview(review: String) {

        var name = tinyDB.getString(K.USER_NAME)
        var img = tinyDB.getString(K.USER_IMG)
        var emailId = tinyDB.getString(K.EMAIL)

        val separated: List<String> = emailId!!.split("@")
        val nodeName = separated[0]
        val time = System.currentTimeMillis()
        Log.d("TIME_MI","${time} ")

        val result = StringBuilder()
        result.append(nodeName).append(time)
        Log.d("TIME_MI","${result} ")


        Log.d("DATA_TO_UPLOAD","$review  $name  $img")
        val random: Int = Random().nextInt(61) + 20


            val cartItem = UploadReview(review,name,img)
            val rootRef = FirebaseDatabase.getInstance().reference
            val yourRef = rootRef.child("Product").child("${productIdGlobal.trim()}").child("review").child(result.toString())
            yourRef.setValue(cartItem)
        (context as WriteReviewScreen).finish()

    }



}