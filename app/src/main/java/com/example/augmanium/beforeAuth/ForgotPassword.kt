package com.example.augmanium.beforeAuth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
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
    var ret = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)
        tinyDB = TinyDB(this)

//        ret = tinyDB.getInt(K.RET_FAC_REC)
//        if ( ret == 1){
//            Toast.makeText(this, "Face not match...", Toast.LENGTH_SHORT).show()
//        }

        tinyDB.clear()
        database = FirebaseDatabase.getInstance().reference
        binding.scanFace.setOnClickListener {
            val emailValid = binding.email.text.toString()
            if(!emailValid.isValidEmail()){
                Toast.makeText(this, "Email not valid", Toast.LENGTH_SHORT).show()
            }else{
            forgotPassword()
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }


    fun String.isValidEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(this).matches()
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
                    if (snapshot.child("User Data").child("email").value==email) {
                        var userImage = snapshot.child("Image").child("imgUrl").value
                        tinyDB.putString(K.USER_IMG,userImage.toString())
                        tinyDB.putString(K.EMAIL_FORGOT,email)
                        //do ur stuff
                        Log.d("Check_EMAIL", "Exist    $userImage  $email")
                        val intent = Intent(this@ForgotPassword, ScanFace::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@ForgotPassword, "User not exist", Toast.LENGTH_SHORT).show()
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

