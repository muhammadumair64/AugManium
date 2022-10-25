package com.example.augmanium.extra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.beforeAuth.signin.SignIn
import com.example.augmanium.databinding.ActivityCreateNewPasswordBinding
import com.example.augmanium.utils.TinyDB

class CreateNewPassword : AppCompatActivity() {

    lateinit var binding: ActivityCreateNewPasswordBinding
    lateinit var tinyDB: TinyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_password)

        tinyDB = TinyDB(this)
        binding.login.setOnClickListener {
            var intent = Intent(this@CreateNewPassword, SignIn::class.java)
            startActivity(intent)
            tinyDB.clear()
            finish()
        }

    }
}