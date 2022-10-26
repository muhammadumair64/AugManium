package com.example.augmanium.afterAuth.checkout.checkOutSummary.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.augmanium.afterAuth.orderCompletionScreen.OrderCompleteScreen
import com.example.augmanium.afterAuth.fragments.cartFragment.dataClass.CartFragmentDataClass
import com.example.augmanium.afterAuth.checkout.CheckOutAddress
import com.example.augmanium.afterAuth.checkout.checkOutSummary.CheckoutSummary
import com.example.augmanium.afterAuth.checkout.checkOutSummary.adapter.SummaryRVAdapter
import com.example.augmanium.afterAuth.checkout.checkOutSummary.dataClass.OrderDataClass
import com.example.augmanium.afterAuth.checkout.checkOutSummary.dataClass.StatusDataClass
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



    fun viewsOfSummaryScreen(
        context: Context,
        binding: ActivityCheckoutSummaryBinding,
        database: DatabaseReference
    ) {

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


            recyclerViewInitializer(binding,context,database)
        }


        binding.changeAddress.setOnClickListener {
            val intent = Intent(context,CheckOutAddress::class.java)
            context.startActivity(intent)
        }
        binding.backButton.setOnClickListener {
            (context as CheckoutSummary).finish()
        }

        binding.backBtn.setOnClickListener {
            (context as CheckoutSummary).finish()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun recyclerViewInitializer(binding: ActivityCheckoutSummaryBinding, context: Context,database: DatabaseReference) {
        Log.d("SummaryScreenTesting", "In function")
        val address = tinyDB.getString(K.ADDRESS)
binding.Address.text = address.toString()
        binding.summaryRV.also {
            it.adapter = SummaryRVAdapter(summaryArrayList)
            it.setHasFixedSize(true)
//            it.adapter!!.notifyDataSetChanged()
        }

        binding.checkoutBtn.setOnClickListener {
            uploadOrder(context,database)

        }

    }




    fun uploadOrder(context: Context, database: DatabaseReference){

        val millis = System.currentTimeMillis()
        tinyDB.putString(K.Order, millis.toString())
        var email = tinyDB.getString(K.EMAIL)
        if (email != null) {
            email = email.split("@").toTypedArray()[0]
        }

       val order = OrderDataClass(totalPrice,millis.toString(),getTime(),summaryArrayList)
        val location = LatLongDataClass()
        val status = StatusDataClass(getTimeForStatus())
        email?.let { database.child("User").child(it).child("Orders").child(millis.toString()).setValue(order)
            database.child("User").child(it).child("Orders").child(millis.toString()).child("location").setValue(location)
            database.child("User").child(it).child("Orders").child(millis.toString()).child("status").setValue(status)

        }

        viewModelScope.launch {
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try{
                        val intent = Intent(context, OrderCompleteScreen::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                        (context as CheckoutSummary).finish()
                    }catch (e:Exception){
                        Log.d("orderPlace","Error : $e")
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message

                }
            }

            database.addValueEventListener(postListener)



        }

    }



    fun getTime(): String {
        val c : Calendar = Calendar.getInstance()
        var df : SimpleDateFormat? = null
        var formattedDate = ""

// goes to main method or onCreate(Android)
        df = SimpleDateFormat("dd-MM-yyyy HH:mm a")
        formattedDate = df.format(c.time)
        println("Format dateTime => $formattedDate")
        return formattedDate
    }

    fun getTimeForStatus(): String {
        val c : Calendar = Calendar.getInstance()
        var df : SimpleDateFormat? = null
        var formattedDate = ""

// goes to main method or onCreate(Android)
        df = SimpleDateFormat("MM/dd")
        formattedDate = df.format(c.time)
        println("Format dateTime => $formattedDate")
        return formattedDate
    }
}