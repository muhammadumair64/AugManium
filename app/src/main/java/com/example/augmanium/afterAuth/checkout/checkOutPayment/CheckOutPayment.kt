package com.example.augmanium.afterAuth.checkout.checkOutPayment

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.databinding.ActivityCheckOutPaymentBinding
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.RenderScriptBlur

@AndroidEntryPoint
class CheckOutPayment : AppCompatActivity() {
    val checkOutPaymentViewModel: CheckOutPaymentViewModel by viewModels()
    lateinit var binding : ActivityCheckOutPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_out_payment)

        checkOutPaymentViewModel.paymentScreenViews(this,binding)
        blurscreen()
    }

    fun blurscreen() {
        val radius = 0.1f

        val decorView: View = window.decorView

        val windowBackground: Drawable = decorView.background
        binding.blurView.setupWith(decorView.findViewById(android.R.id.content))
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)

    }
}