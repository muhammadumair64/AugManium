package com.example.augmanium.beforeAuth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.databinding.ActivityForgotPasswordBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ForgotPassword : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding
    lateinit var tinyDB: TinyDB
    lateinit var database: com.google.firebase.database.DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)
        tinyDB = TinyDB(this)
        tinyDB.clear()
        database = FirebaseDatabase.getInstance().reference
        binding.scanFace.setOnClickListener {
            forgotPassword()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    fun forgotPassword(){

        val email = binding.email.text.toString().trim()
        val mAuth: FirebaseAuth
        mAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.getCurrentUser()
        val UID = user?.uid
        val separated: List<String> = email!!.split("@")
        val nodeName = separated[0]


        database.child("User").child(nodeName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("email").value==email) {
                        var userImage = snapshot.child("Image").child("imgUrl").value
                        tinyDB.putString(K.USER_IMG,userImage.toString())
                        tinyDB.putString(K.EMAIL_FORGOT,email)
                        //do ur stuff
                        Log.d("Check_EMAIL", "Exist    $userImage  $email")
                        val intent = Intent(this@ForgotPassword, ScanFace::class.java)
                        startActivity(intent)
                    } else {
                        //do something if not exists
                        Log.d("Check_EMAIL", "NOT_Exist    ${snapshot}  ${snapshot.child("email")}")
                    }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}

