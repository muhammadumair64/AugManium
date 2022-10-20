package com.example.augmanium.beforeAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.beforeAuth.authViewModel.AuthViewModel
import com.example.augmanium.beforeAuth.signin.SignIn
import com.example.augmanium.databinding.ActivitySignUpBinding
import com.example.augmanium.utils.K
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
            binding.progressLayout.visibility= View.VISIBLE
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


        binding.showPassBtn.setOnClickListener {

            if (binding.editPassword.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
                binding.editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())

                val uri = "@drawable/icon_hide_password" // where myresource (without the extension) is the file
                val imageResource = resources.getIdentifier(uri, null, packageName)
                val res = resources.getDrawable(imageResource)
                binding.showPassBtn.setImageDrawable(res)

            } else {
                binding.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
//                binding.showPassBtn.setImageDrawable(this.getDrawable(R.drawable.));
//                binding.showPassBtn.setImageResource(R.drawable.)


                val uri = "@drawable/ic_icon_awesome_eye" // where myresource (without the extension) is the file


                val imageResource = resources.getIdentifier(uri, null, packageName)
                val res = resources.getDrawable(imageResource)
                binding.showPassBtn.setImageDrawable(res)


            }
        }


    }


}