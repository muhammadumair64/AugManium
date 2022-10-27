package com.example.augmanium.afterAuth.checkout.checkOutSummary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.augmanium.R
import com.example.augmanium.afterAuth.checkout.checkOutSummary.viewModel.SummaryViewModel
import com.example.augmanium.afterAuth.orderCompletionScreen.OrderCompleteScreen
import com.example.augmanium.databinding.ActivityCheckoutSummaryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

@AndroidEntryPoint
class CheckoutSummary : AppCompatActivity() {
    lateinit var binding: ActivityCheckoutSummaryBinding
    lateinit var database: DatabaseReference
    var nextCheck= false
    val viewModel: SummaryViewModel by viewModels<SummaryViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_summary)


        initViews()
    }

    fun initViews(){
        nextCheck = true
        database = FirebaseDatabase.getInstance().reference
        viewModel.viewsOfSummaryScreen(this,binding,database)
viewModel.nextCheck = true
    }

 fun moveToNext(){
     Log.d("SummaryScreenTAG"," In next block")
     if(nextCheck){
         Timer().schedule(1000) {
             lifecycleScope.launch(Dispatchers.Main){
                 val intent = Intent(this@CheckoutSummary, OrderCompleteScreen::class.java)
                 intent.flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                 nextCheck = false
                 finishAffinity()
                 startActivity(intent)

//         viewModel.nextCheck = false
                 finish()
             }
         }
     }




 }



}
