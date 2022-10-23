package com.example.augmanium.afterAuth.orderHistory.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.augmanium.afterAuth.trackOrder.TrackOrder
import com.example.augmanium.afterAuth.checkout.checkOutSummary.dataClass.OrderDataClass
import com.example.augmanium.afterAuth.orderHistory.OrderHistory
import com.example.augmanium.afterAuth.orderHistory.adapter.OrderHistoryAdapter
import com.example.augmanium.afterAuth.orderHistory.dataClass.OrderHistoryDataClass
import com.example.augmanium.afterAuth.orderHistory.orderInterface.OnItemClicked
import com.example.augmanium.databinding.ActivityOrderHistoryBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(): ViewModel() , OnItemClicked{
    private lateinit var database: DatabaseReference
    val orderHistoryArrayList: ArrayList<OrderHistoryDataClass> = ArrayList()
    lateinit var tinyDb: TinyDB
    lateinit var activityContext :Context
    fun orderHistoryRv(binding: ActivityOrderHistoryBinding, context: Context) {
        database = FirebaseDatabase.getInstance().reference
        activityContext = context
        tinyDb = TinyDB(context)

        binding.progressLayout.visibility= View.VISIBLE

        binding.backButton.setOnClickListener {
            (context as OrderHistory).finish()
        }

//        orderHistoryArrayList.add(OrderHistoryDataClass("Order No: 9980713", "1200$", "9 Jun , 2022 9:57 PM"))
//        orderHistoryArrayList.add(OrderHistoryDataClass("Order No: 9980713", "1200$", "9 Jun , 2022 9:57 PM"))
//        orderHistoryArrayList.add(OrderHistoryDataClass("Order No: 9980713", "1200$", "9 Jun , 2022 9:57 PM"))
//        orderHistoryArrayList.add(OrderHistoryDataClass("Order No: 9980713", "1200$", "9 Jun , 2022 9:57 PM"))
//        orderHistoryArrayList.add(OrderHistoryDataClass("Order No: 9980713", "1200$", "9 Jun , 2022 9:57 PM"))

//        binding.orderHistoryRv.also {
//            it.adapter = OrderHistoryAdapter(orderHistoryArrayList)
//            it.setHasFixedSize(true)
//        }

viewModelScope.launch {
    var email =  tinyDb.getString(K.EMAIL)
    if (email != null) {
        email = email.split("@").toTypedArray()[0]
    }
    val postListener = object : ValueEventListener {


        override fun onDataChange(dataSnapshot: DataSnapshot) {
            println("DataTesting parent is ${dataSnapshot.key}  ")
            for (snapshot in dataSnapshot.children) {

                val node = snapshot.getValue(OrderDataClass::class.java)
                if (node != null) {
                    orderHistoryArrayList.add(
                        OrderHistoryDataClass(node.OrderID,(node.totalPrice).toString(),node.time)
                    )

                    binding.progressLayout.visibility=View.INVISIBLE
                }


                Log.d("DataTesting","$snapshot")
            }


            binding.orderHistoryRv.also {
                it.adapter =OrderHistoryAdapter(orderHistoryArrayList,this@OrderHistoryViewModel)
                it.setHasFixedSize(true)

            }



        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("DataTesting","Error : $error")

        }

    }

    if (email != null) {
        database.child("User").child(email).child("Orders").addValueEventListener(postListener)
    }


}












    }

    override fun onClick(position: Int) {
      val order = orderHistoryArrayList[position]

            tinyDb.putString(K.Order, order.orderNumber)

        val intent = Intent(activityContext, TrackOrder::class.java)
        ContextCompat.startActivity(activityContext, intent, null)
    }

}