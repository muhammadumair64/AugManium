package com.example.augmanium.beforeAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.beforeAuth.authViewModel.AuthViewModel
import com.example.augmanium.beforeAuth.signin.SignIn
import com.example.augmanium.databinding.ActivitySignUpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUp : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding



    val viewModel: AuthViewModel by viewModels<AuthViewModel>()


    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)


        database = FirebaseDatabase.getInstance().reference
        binding.signupBtn.setOnClickListener {
            viewModel.signUp(binding,this,database)
        }

        binding.signIn.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }

        binding.signInBtnTop.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }
        binding.backButton.setOnClickListener {
            finish()
        }

    }


}