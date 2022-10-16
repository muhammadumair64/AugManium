package com.example.augmanium.afterAuth.checkout.checkOutSummary.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.augmanium.afterAuth.cartFragment.dataClass.CartFragmentDataClass
import com.example.augmanium.afterAuth.checkout.checkOutSummary.adapter.SummaryRVAdapter
import com.example.augmanium.afterAuth.checkout.checkOutSummary.dataClass.OrderDataClass
import com.example.augmanium.afterAuth.checkout.checkOutSummary.dataClass.SummaryDataClass
import com.example.augmanium.databinding.ActivityCheckoutSummaryBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.*
import com.example.augmanium.afterAuth.location.LatLongDataClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class SummaryViewModel @Inject constructor() : ViewModel() {
    var cartFragmentArrayList: ArrayList<CartFragmentDataClass> = ArrayList()
    var summaryArrayList: ArrayList<SummaryDataClass> = ArrayList()
    lateinit var tinyDB: TinyDB
    var totalPrice = 0
    private lateinit var database: DatabaseReference


    fun viewsOfSummaryScreen(context: Context, binding: ActivityCheckoutSummaryBinding) {
        database = FirebaseDatabase.getInstance().reference
        tinyDB = TinyDB(context)
        viewModelScope.launch {
            cartFragmentArrayList = tinyDB.getListObject(
                K.CART,
                CartFragmentDataClass::class.java
            ) as ArrayList<CartFragmentDataClass>
            Log.d("SummaryScreenTesting", "${cartFragmentArrayList.size}")
            for (product in cartFragmentArrayList) {
                try {
                    Log.d(
                        "SummaryScreenTesting",
                        "Running : ${product.image!!},  ${product.productName!!},${product.productPrice!!}"
                    )

                    val separatedPrice = product.productPrice.split("$").toTypedArray()[0]
                    totalPrice += (separatedPrice).toInt()
                    summaryArrayList.add(
                        SummaryDataClass(
                            product.image!!,
                            product.productName!!,
                            product.productPrice!!
                        )
                    )

                } catch (e: Exception) {
                    Log.d("SummaryScreenTesting", " Error : $e")

                }
            }


            recyclerViewInitializer(binding,context)
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    fun recyclerViewInitializer(binding: ActivityCheckoutSummaryBinding, context: Context) {
        Log.d("SummaryScreenTesting", "In function")

        binding.summaryRV.also {
            it.adapter = SummaryRVAdapter(summaryArrayList)
            it.setHasFixedSize(true)
//            it.adapter!!.notifyDataSetChanged()
        }

        binding.checkoutBtn.setOnClickListener {
            uploadOrder(context)

        }

    }




    fun uploadOrder(context: Context){

        val millis = System.currentTimeMillis()
        var email = tinyDB.getString(K.EMAIL)
        if (email != null) {
            email = email.split("@").toTypedArray()[0]
        }

       val order = OrderDataClass(totalPrice,millis.toString(),getTime(),summaryArrayList,"0")
        val location = LatLongDataClass()
        email?.let { database.child("User").child(it).child("Orders").child(millis.toString()).setValue(order)
            database.child("User").child(it).child("Orders").child(millis.toString()).child("location").setValue(location)
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Toast.makeText(context, "Order Place", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }

        database.addValueEventListener(postListener)



    }

    fun getTime(): String {
        val c : Calendar = Calendar.getInstance()
        var df : SimpleDateFormat? = null
        var formattedDate = ""

// goes to main method or onCreate(Android)
        df = SimpleDateFormat("dd-MM-yyyy HH:mm a")
        formattedDate = df!!.format(c.time)
        println("Format dateTime => $formattedDate")
        return formattedDate
    }
}