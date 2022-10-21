package com.example.augmanium.extra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.beforeAuth.signin.SignIn
import com.example.augmanium.databinding.ActivityCreateNewPasswordBinding

class CreateNewPassword : AppCompatActivity() {

    lateinit var binding: ActivityCreateNewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_password)

        binding.login.setOnClickListener {
            var intent = Intent(this@CreateNewPassword, SignIn::class.java)
            startActivity(intent)
            finish()
        }

    }
}