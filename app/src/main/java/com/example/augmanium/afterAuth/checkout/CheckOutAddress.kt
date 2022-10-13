package com.example.augmanium.afterAuth.checkout

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.augmanium.ModelActivity
import com.example.augmanium.R
import com.example.augmanium.afterAuth.checkout.checkOutPayment.CheckOutPayment
import com.example.augmanium.databinding.ActivityCheckOutAddressBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.MyFirebaseMessagingService
import com.example.augmanium.utils.TinyDB

class CheckOutAddress : AppCompatActivity() {
    lateinit var binding: ActivityCheckOutAddressBinding
    lateinit var tinyDB: TinyDB
    var context :Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tinyDB = TinyDB(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_out_address)

        val tokan = MyFirebaseMessagingService.getToken(this)
        if (tokan != null) {
            Log.d("AddressTesting",tokan)
        }
        getDataFunction()
    }


    fun getDataFunction() {

        binding.applyBtn.setOnClickListener {
            binding.apply {

                if (home.text.equals("") && streetDetail.text.equals("") && nameEditText.text.equals(
                        ""
                    ) && cityNameEditText.text.equals("") && stateNameEditText.text.equals("")
                ) {
                    Log.d("AddressTesting","value is empty ")
                         showToast()
                } else {

                  val value = "${home.text}:${streetDetail.text},${nameEditText.text},${cityNameEditText.text},${stateNameEditText.text}"
                      tinyDB.putString(K.ADDRESS,value)

                    Log.d("AddressTesting","$value")
                    val intent = Intent(context , CheckOutPayment::class.java)
                    startActivity(intent)

                }
            }


        }


    }


    fun showToast() {
        Toast.makeText(this, "Fields Missing", Toast.LENGTH_SHORT).show()
    }
}