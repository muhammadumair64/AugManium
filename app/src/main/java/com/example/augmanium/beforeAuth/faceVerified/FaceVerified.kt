package com.example.augmanium.beforeAuth.faceVerified

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.augmanium.R

import com.example.augmanium.dataClass.UserImage
import com.example.augmanium.dataClass.UserInfo
import com.example.augmanium.databinding.ActivityFaceVerifiedBinding
import com.example.augmanium.extra.CreateNewPassword
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FaceVerified : AppCompatActivity() {

    lateinit var binding: ActivityFaceVerifiedBinding
    lateinit var tinyDB: TinyDB
    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference


    var TAG = "FACE_VERIFIED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_face_verified)
        tinyDB = TinyDB(this)

        viewBinding()

    }

    fun viewBinding(){


        val email = tinyDB.getString(K.EMAIL_FORGOT)

        val separated: List<String> = email!!.split("@")
        val nodeName = separated[0]
        getUser(nodeName)
        getUserImage(nodeName)
        Log.d("FACE_REC_RESULT", "Same face $email")

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.ContinueBtn.setOnClickListener {
            forgotPass(email)
        }

    }

    fun forgotPass(email: String){

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")

                    val intent = Intent(this, CreateNewPassword::class.java)
                    startActivity(intent)
                    finish()
                }
                else if (task.isCanceled){
                    Log.d(TAG, "Email sent. Canceled")
                    finish()
                }else{
                    Log.d(TAG, "Email sent. ${task.exception!!.localizedMessage}")
                    finish()
                }
            }

    }


    fun getUser(nodeName: String){
        database.child("User").child(nodeName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserInfo::class.java)
                if (user != null) {
                    Log.d(TAG,"USER ${user.userName}")
                    binding.Username.text = user.userName
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")
            }
        })
    }

    fun getUserImage(nodeName: String) {
        Log.d("IMAGE User ","${nodeName}")
        database.child("User").child(nodeName).child("Image").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("IMAGE User ","${snapshot.key}   ${snapshot.value}")
                val user = snapshot.getValue(UserImage::class.java)
                if (user != null) {
                    Log.d(TAG,"USER ${user.imgUrl}")

                    Glide.with(this@FaceVerified)
                        .load(user.imgUrl)
                        .override(300, 200)
                        .into(binding.userImage);
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")
            }
        })
    }



}