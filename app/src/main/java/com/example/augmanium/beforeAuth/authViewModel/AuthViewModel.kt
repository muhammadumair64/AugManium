package com.example.augmanium.beforeAuth.authViewModel

import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.augmanium.EditProfile
import com.example.augmanium.beforeAuth.fireBase.Extensions.toast
import com.example.augmanium.beforeAuth.fireBase.FirebaseUtils
import com.example.augmanium.databinding.ActivitySignUpBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(): ViewModel()  {

    lateinit var signInInputsArray: Array<EditText>
    lateinit var tinyDB: TinyDB

    lateinit var userEmail :String
    lateinit var userPassword :String
    lateinit var userName : String
    val auth = Firebase.auth

    private fun notEmpty(): Boolean = userEmail.isNotEmpty() && userPassword.isNotEmpty() && userName.isNotEmpty()
    fun signUp(binding: ActivitySignUpBinding, context: Context) {

        tinyDB = TinyDB(context)
        tinyDB.clear()
        signInInputsArray = arrayOf(binding.email, binding.editPassword, binding.name)

        // identicalPassword() returns true only  when inputs are not empty and passwords are identical
        userEmail = binding.email.text.toString().trim()
        userPassword = binding.editPassword.text.toString().trim()
        userName = binding.name.text.toString().trim()
        println("User name: $userName")
        tinyDB.putString(K.USER_NAME,userName)
        tinyDB.putString(K.PASSWORD,userPassword)
        tinyDB.putString(K.EMAIL,userEmail)


        /*create a user*/
        if(notEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                FirebaseUtils.firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            Toast.makeText(context, "created account successfully !", Toast.LENGTH_SHORT).show()
                            tinyDB.putInt(K.SIGN_UP,1)
                            val intent = Intent(context, EditProfile::class.java)
                           ActivityCompat.startActivity(context,intent,null)
//                            ActivityCompat.finish()
                        } else {
                            Toast.makeText(context, "failed to Authenticate !", Toast.LENGTH_SHORT).show()
                            println("error on signup.... " + task.exception)


                        }
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



}