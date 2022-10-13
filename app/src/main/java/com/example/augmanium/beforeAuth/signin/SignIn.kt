package com.example.augmanium.beforeAuth.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.augmanium.ModelActivity
import com.example.augmanium.afterAuth.mainActivity.MainActivity
import com.example.augmanium.beforeAuth.ForgotPassword
import com.example.augmanium.beforeAuth.SignUp
import com.example.augmanium.beforeAuth.authViewModel.AuthViewModel
import com.example.augmanium.beforeAuth.fireBase.Extensions.toast
import com.example.augmanium.beforeAuth.fireBase.FirebaseUtils.firebaseAuth
import com.example.augmanium.databinding.ActivitySignInBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.firebase.ui.auth.AuthUI
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignIn : AppCompatActivity() {

    lateinit var binding: ActivitySignInBinding
    lateinit var signInEmail : String
    lateinit var signInPassword : String
    lateinit var signInInputsArray: Array<EditText>
    lateinit var tinyDB: TinyDB
    private lateinit var database: DatabaseReference
    val viewModel: AuthViewModel by viewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, com.example.augmanium.R.layout.activity_sign_in)
        database = FirebaseDatabase.getInstance().reference

        signInInputsArray = arrayOf(binding.email, binding.editPassword)
        tinyDB = TinyDB(this)

        binding.signInBtn.setOnClickListener {
            signInUser()
        }
        binding.signUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        binding.signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        binding.forgetPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
        binding.backButton.setOnClickListener {
            finish()
        }

    }


    fun onClick() {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener { // user is now signed out
//                    startActivity(Intent(this@MyActivity, SignInActivity::class.java))
//                    finish()
                    Toast.makeText(this@SignIn,"Sign out done",Toast.LENGTH_SHORT).show()
                    Log.d("signout ","Done")

                }.addOnFailureListener {
                    Log.d("Faild_to_signout"," ${it.localizedMessage}")
                }
    }








    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()







    private fun signInUser() {
        signInEmail = binding.email.text.toString().trim()
        signInPassword = binding.editPassword.text.toString().trim()

        if (notEmpty()) {
            lifecycleScope.launch(Dispatchers.IO) {
               firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                   .addOnCompleteListener { signIn ->
                       if (signIn.isSuccessful) {
                           tinyDB.putString(K.EMAIL,signInEmail)
                           viewModel.uploadFCMToken(signInEmail,database,this@SignIn)

//
                           val intent = Intent(this@SignIn, MainActivity::class.java)
                           startActivity(intent)

//
//                           val intent = Intent(this@SignIn, ModelActivity::class.java)
//                           startActivity(intent)
                       }

                       else {

                           Log.d("Login_Exception "," ${signIn.exception}")

                           toast("sign in failed")
                       }
                   }
                   .addOnFailureListener {
                       Log.d("Faild_to_login"," ${it.localizedMessage}")
                   }
           }
           }
        else {
            signInInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }

            }

    }


