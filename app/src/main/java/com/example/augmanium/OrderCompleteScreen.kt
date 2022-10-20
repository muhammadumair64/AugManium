package com.example.augmanium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.augmanium.afterAuth.searchscreen.SearchActivity
import com.example.augmanium.databinding.ActivityOrderCompleteScreenBinding
import com.example.augmanium.databinding.FragmentProfileBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB

class OrderCompleteScreen : AppCompatActivity() {

    lateinit var binding: ActivityOrderCompleteScreenBinding
lateinit var tinyDb :TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_order_complete_screen)
           tinyDb= TinyDB(this)
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.trackOrderBtn.setOnClickListener {
            val intent = Intent(this,TrackOrder::class.java)
            startActivity(intent)
        }
      val order =  tinyDb.getString(K.Order)
      binding.orderNumber.text = "Your Order No. $order has been placed"


//        binding.searchBtn.setOnClickListener {
//            val intent = Intent(this, SearchActivity::class.java)
//            startActivity(intent)
//        }

    }
}