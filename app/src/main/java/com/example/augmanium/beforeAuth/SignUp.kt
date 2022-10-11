package com.example.augmanium.beforeAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.augmanium.EditProfile
import com.example.augmanium.R
import com.example.augmanium.beforeAuth.authViewModel.AuthViewModel
import com.example.augmanium.beforeAuth.fireBase.Extensions.toast
import com.example.augmanium.beforeAuth.fireBase.FirebaseUtils.firebaseAuth
import com.example.augmanium.beforeAuth.fireBase.FirebaseUtils.firebaseUser
import com.example.augmanium.beforeAuth.signin.SignIn
import com.example.augmanium.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUp : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding



    val viewModel: AuthViewModel by viewModels<AuthViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)



        binding.signupBtn.setOnClickListener {
            viewModel.signUp(binding,this)
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