package com.example.augmanium.beforeAuth

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.augmanium.R

import android.content.Intent
import com.example.augmanium.beforeAuth.signin.signIn


class SplashScreen : AppCompatActivity() {
    lateinit var continueBtn : RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        initViews()
    }

   fun initViews(){
        continueBtn = findViewById(R.id.continueBtn)

       continueBtn.setOnClickListener {
           val send = Intent(this, signIn::class.java)
           startActivity(send)
       }
    }
}