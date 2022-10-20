package com.example.augmanium.afterAuth.checkout.checkOutPayment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.augmanium.afterAuth.checkout.checkOutSummary.CheckoutSummary
import com.example.augmanium.afterAuth.searchscreen.SearchActivity
import com.example.augmanium.databinding.ActivityCheckOutPaymentBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckOutPaymentViewModel @Inject constructor(): ViewModel() {

    var prevLength = -1
    fun paymentScreenViews(context: Context, binding:ActivityCheckOutPaymentBinding) {
        binding.notCard.setOnClickListener {
            binding.blurView.setVisibility(View.INVISIBLE)
            binding.ByCard.setVisibility(View.VISIBLE)
            binding.notCard.setVisibility(View.INVISIBLE)
            binding.byCash.setVisibility(View.INVISIBLE)
            binding.notCash.setVisibility(View.VISIBLE)

            binding.name.isEnabled=true
            binding.cardNumberEditText.isEnabled=true
            binding.cvvEditText.isEnabled=true
            binding.editTextExpireDate.isEnabled=true


        }


        binding.notCash.setOnClickListener {

            binding.blurView.setVisibility(View.VISIBLE)
            binding.ByCard.setVisibility(View.INVISIBLE)
            binding.notCard.setVisibility(View.VISIBLE)
            binding.byCash.setVisibility(View.VISIBLE)
            binding.notCash.setVisibility(View.INVISIBLE)

            binding.name.isEnabled=false
            binding.cardNumberEditText.isEnabled=false
            binding.cvvEditText.isEnabled=false
            binding.editTextExpireDate.isEnabled=false

        }


        var count = 0

        binding.cardNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                var inputlength: Int = binding.cardNumberEditText.getText().toString().length
                if ( inputlength == 4 ||
                    inputlength == 9 || inputlength == 14
                ) {
                    if (prevLength != 5 && prevLength != 10&&  prevLength != 15) {


                        Log.d("DEBUG", "${text.length}")
                        binding.cardNumberEditText.setText(
                            binding.cardNumberEditText.getText().toString() + " "
                        )
                    }
                }
                Log.d("DEBUG 2","${text.length}")
                val pos: Int = binding.cardNumberEditText.getText().length
                prevLength = pos
                if(pos!= -1 ){
                    binding.cardNumberEditText.setSelection(pos)
                }
            }
        })


        binding.buttonNext.setOnClickListener {
            val intent = Intent(context, CheckoutSummary::class.java)
            context.startActivity(intent)
        }
        binding.backButton.setOnClickListener {
            (context as CheckOutPayment).finish()
        }
        binding.backBtn.setOnClickListener {
            (context as CheckOutPayment).finish()
        }

    }
}