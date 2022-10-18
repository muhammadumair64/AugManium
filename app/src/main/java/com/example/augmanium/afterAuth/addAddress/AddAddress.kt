package com.example.augmanium.afterAuth.addAddress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.databinding.ActivityAddAddressBinding

class AddAddress : AppCompatActivity() {

    lateinit var binding :ActivityAddAddressBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_address)



    }
}