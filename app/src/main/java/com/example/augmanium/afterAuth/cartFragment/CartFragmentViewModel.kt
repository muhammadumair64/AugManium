package com.example.augmanium.afterAuth.cartFragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.augmanium.afterAuth.cartFragment.adapter.CartFragmentAdapter
import com.example.augmanium.afterAuth.cartFragment.dataClass.CartFragmentDataClass
import com.example.augmanium.afterAuth.cartFragment.interfaceForClick.CartRVClick
import com.example.augmanium.afterAuth.searchscreen.SearchActivity
import com.example.augmanium.databinding.FragmentCartBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartFragmentViewModel @Inject constructor() : ViewModel(), CartRVClick {

    lateinit var activityBinding: FragmentCartBinding
    lateinit var activitycontext: Context
    lateinit var tinyDB: TinyDB
    var email = ""
    var subtotalPrice = 0
    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference
    val cartFragmentArrayList: ArrayList<CartFragmentDataClass> = ArrayList()
    var totalPriceOfProducts = 0
    var shipping = 0



    fun bindView(binding: FragmentCartBinding, context: Context){
        activityBinding = binding
        activitycontext = context
        tinyDB = TinyDB(context)

        binding.checkout.setOnClickListener {
            Log.d("DATA_IS","$cartFragmentArrayList")
        }

        binding.searchBtn.setOnClickListener {
            val intent = Intent(activitycontext,SearchActivity::class.java)
            activitycontext.startActivity(intent)
        }

        getCartData()

    }

    fun getCartData(){
        cartFragmentArrayList.clear()
        email = tinyDB.getString(K.EMAIL).toString()
        val separated: List<String> = email!!.split("@")
        val nodeName = separated[0]

        database.child("Cart").child(nodeName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.getChildren()) {
                    Log.d("NODE___"," ${snap.key} $snapshot")
//                    for (products in snap.children) {
                    Log.d("NODE___"," ${snap.key} $snap")
                    var cartData = snap.getValue(CartFragmentDataClass::class.java)
                    var price = cartData!!.productPrice
                    val separatedPrice = price!!.split("$").toTypedArray()[0]
                    var totalPrice = separatedPrice.toInt()

                    var shippingFeePerItem = 20
                    var count = cartData.productCount?.trim()
                    shipping = shipping + shippingFeePerItem
               val currentPrize = totalPrice * (count?.toInt()!!)
                    totalPriceOfProducts = totalPriceOfProducts + currentPrize
                    Log.d("PRODUCT_PRICE","$totalPriceOfProducts")
                    cartFragmentArrayList.add(cartData!!)
                    Log.d("CART_DATA___"," ${cartFragmentArrayList}")
                    rv()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")
            }

        })

    }

    fun rv(){
        tinyDB.putListObject(K.CART,cartFragmentArrayList as ArrayList<Object>)
        activityBinding.cartProduct.also {
            it.adapter = CartFragmentAdapter(cartFragmentArrayList,this@CartFragmentViewModel)
            it.setHasFixedSize(true)
            it.adapter!!.notifyDataSetChanged()
        }

        activityBinding.subTotalPrice.text = totalPriceOfProducts.toString()
        activityBinding.shippingPrice.text = shipping.toString()
    }

    override fun onDlt(position: Int) {


        val separated: List<String> = email!!.split("@")
        val nodeName = separated[0]
        val product = cartFragmentArrayList[position]
        val alert = AlertDialog.Builder(activitycontext)
        alert.setTitle("Alert")
        alert.setMessage("Click okay to confirm to delete")
//        alert.setIcon(R.drawable.item_)
        alert.setPositiveButton("yes") { dialoge, id ->
            FirebaseDatabase.getInstance().reference.child("Cart").child(nodeName)
                .child(product.productName!!)
                .removeValue()
            getCartData()
            Toast.makeText(activitycontext, "Item deleted", Toast.LENGTH_SHORT).show()
        }
        alert.setNegativeButton("no") { dialoge, id ->
            Toast.makeText(activitycontext, "cancel", Toast.LENGTH_SHORT).show()
        }
        alert.show()
    }


}