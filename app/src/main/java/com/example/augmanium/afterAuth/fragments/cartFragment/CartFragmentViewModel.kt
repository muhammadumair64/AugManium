package com.example.augmanium.afterAuth.fragments.cartFragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.augmanium.afterAuth.fragments.cartFragment.adapter.CartFragmentAdapter
import com.example.augmanium.afterAuth.fragments.cartFragment.dataClass.CartFragmentDataClass
import com.example.augmanium.afterAuth.fragments.cartFragment.interfaceForClick.CartRVClick
import com.example.augmanium.afterAuth.checkout.CheckOutAddress
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

        binding.checkout.setOnClickListener {
            val intent = Intent(activitycontext,CheckOutAddress::class.java)
            activitycontext.startActivity(intent)
        }
        getCartData()

    }

    fun getCartData(){
        totalPriceOfProducts = 0
        cartFragmentArrayList.clear()
        activityBinding.checkout.visibility=View.INVISIBLE
        activityBinding.progressLayout.visibility= View.VISIBLE
        email = tinyDB.getString(K.EMAIL).toString()
        val separated: List<String> = email!!.split("@")
        val nodeName = separated[0]

        database.child("Cart").child(nodeName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    Log.d("NODE___CART"," ${snap.key} $snapshot")
//                    for (products in snap.children) {
                    Log.d("NODE___"," ${snap.key} $snap")
                    val cartData = snap.getValue(CartFragmentDataClass::class.java)
//

                    val price = cartData?.productPrice
                    if(price != null){
                        val separatedPrice = price.split("$").toTypedArray()[0]
                        val totalPrice = separatedPrice.toInt()
                        val count = cartData.productCount?.trim()
                        val currentPrize = totalPrice * (count?.toInt()!!)
                        totalPriceOfProducts += currentPrize
                        Log.d("PRODUCT_PRICE","$totalPriceOfProducts")
                        cartFragmentArrayList.add(cartData)
                        activityBinding.checkout.visibility=View.VISIBLE
                        Log.d("CART_DATA___"," $cartFragmentArrayList")
                    }else{
                        totalPriceOfProducts = 0
                        activityBinding.subTotalPrice.text = totalPriceOfProducts.toString()
                    }

                }
                rv()
                activityBinding.progressLayout.visibility=View.INVISIBLE
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
            Toast.makeText(activitycontext, "Item deleted", Toast.LENGTH_SHORT).show()
            getCartData()

        }
        alert.setNegativeButton("no") { dialoge, id ->
            Toast.makeText(activitycontext, "cancel", Toast.LENGTH_SHORT).show()
        }
        alert.show()
    }


}