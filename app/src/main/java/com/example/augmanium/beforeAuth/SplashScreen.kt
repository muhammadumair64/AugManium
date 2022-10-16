package com.example.augmanium.beforeAuth

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.augmanium.R

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import com.example.augmanium.afterAuth.mainActivity.MainActivity
import com.example.augmanium.beforeAuth.authViewModel.AuthViewModel
import com.example.augmanium.beforeAuth.signin.SignIn
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    lateinit var continueBtn : RelativeLayout
    val viewModel: AuthViewModel by viewModels<AuthViewModel>()
    lateinit var tinyDB: TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        initViews()
    }

   fun initViews(){
       tinyDB = TinyDB(this)
        continueBtn = findViewById(R.id.continueBtn)


       continueBtn.setOnClickListener {

Log.d("CHECKTINY","$tinyDB    ${tinyDB.getString(K.EMAIL)}")
           if (tinyDB.getString(K.EMAIL)!!.isEmpty()){

               val send = Intent(this, SignIn::class.java)
               startActivity(send)


           }else{
               val send = Intent(this, MainActivity::class.java)
               viewModel.getUser(tinyDB.getString(K.EMAIL)!!,this)
               startActivity(send)
               finish()
           }


       }
    }
}