package com.example.augmanium.beforeAuth.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.augmanium.R
import com.example.augmanium.beforeAuth.fireBase.Extensions.toast
import com.example.augmanium.beforeAuth.fireBase.FirebaseUtils.firebaseAuth
import com.example.augmanium.databinding.ActivitySignInBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignIn : AppCompatActivity() {

    lateinit var binding: ActivitySignInBinding
    lateinit var signInEmail : String
    lateinit var signInPassword : String
    lateinit var signInInputsArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in)

        signInInputsArray = arrayOf(binding.email, binding.editPassword)

        binding.signInBtn.setOnClickListener {
            signInUser()
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
//                        startActivity(Intent(this, HomeActivity::class.java))
                           toast("signed in successfully")
                       } else {
                           toast("sign in failed")
                       }
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


