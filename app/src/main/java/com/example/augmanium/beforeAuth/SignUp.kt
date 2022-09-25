package com.example.augmanium.beforeAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.beforeAuth.fireBase.Extensions.toast
import com.example.augmanium.beforeAuth.fireBase.FirebaseUtils.firebaseAuth
import com.example.augmanium.beforeAuth.fireBase.FirebaseUtils.firebaseUser
import com.example.augmanium.beforeAuth.signin.SignIn
import com.example.augmanium.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var signInInputsArray: Array<EditText>

    lateinit var userEmail :String
    lateinit var userPassword :String
    lateinit var userName : String

    val auth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)

        signInInputsArray = arrayOf(binding.email, binding.editPassword, binding.name)

        binding.signupBtn.setOnClickListener {
            signUp()
        }

        binding.signIn.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }

    }

    private fun notEmpty(): Boolean = userEmail.isNotEmpty() && userPassword.isNotEmpty() && userName.isNotEmpty()
    private fun signUp() {

            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
           userEmail = binding.email.text.toString().trim()
           userPassword = binding.editPassword.text.toString().trim()
        userName = binding.name.text.toString().trim()


            /*create a user*/
        if(notEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        toast("created account successfully !")
                        sendEmailVerification(userEmail)
//                        startActivity(Intent(this, HomeActivity::class.java))
//                        finish()
                    } else {

                        toast("failed to Authenticate !")
                        println("error on signup.... " + task.exception)


                    }
                }
        }else{
            signInInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }

    }

    /* send verification email to the new user. This will only
   *  work if the firebase user is not null.
   */

    private fun sendEmailVerification(userEmail: String) {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("email sent to $userEmail")
                }
            }
        }
    }
}