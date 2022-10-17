package com.example.augmanium

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.augmanium.afterAuth.searchscreen.SearchActivity
import com.example.augmanium.databinding.ActivityOrderCompleteScreenBinding
import com.example.augmanium.databinding.FragmentProfileBinding

class OrderCompleteScreen : AppCompatActivity() {

    lateinit var binding: ActivityOrderCompleteScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_order_complete_screen)

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

    }
}