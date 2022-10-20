package com.example.augmanium.afterAuth.addAddress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.addAddress.viewModel.AddAddressViewModel
import com.example.augmanium.databinding.ActivityAddAddressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAddress : AppCompatActivity() {

    lateinit var binding :ActivityAddAddressBinding
    val viewModel : AddAddressViewModel by viewModels<AddAddressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_address)
        viewModel.binding(this,binding)

    }
}