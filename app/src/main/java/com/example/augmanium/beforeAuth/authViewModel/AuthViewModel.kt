package com.example.augmanium.beforeAuth.authViewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.augmanium.afterAuth.editProfile.EditProfile
import com.example.augmanium.beforeAuth.fireBase.FirebaseUtils
import com.example.augmanium.dataClass.UserImage
import com.example.augmanium.dataClass.UserInfo
import com.example.augmanium.databinding.ActivitySignUpBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.MyFirebaseMessagingService
import com.example.augmanium.utils.TinyDB
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
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
    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference
    val auth = Firebase.auth
    val userArrayList: ArrayList<UserInfo> = ArrayList()


    private fun notEmpty(): Boolean = userEmail.isNotEmpty() && userPassword.isNotEmpty() && userName.isNotEmpty()
    fun signUp(binding: ActivitySignUpBinding, context: Context, database: DatabaseReference) {

        tinyDB = TinyDB(context)
        tinyDB.clear()
        signInInputsArray = arrayOf(binding.email, binding.editPassword, binding.name)
        tinyDB.putInt(K.SIGN_UP,1)
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
//                            getUser(userEmail, context)
                            uploadFCMToken(userEmail,database,context)
                            val intent = Intent(context, EditProfile::class.java)
                           ActivityCompat.startActivity(context,intent,null)
                            binding.progressLayout.visibility= View.INVISIBLE
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






    fun uploadFCMToken(email: String, database: DatabaseReference, context: Context) {

        tinyDB = TinyDB(context)
        val value = email.split("@").toTypedArray()[0]
        database.child("User").child(value).child("FCM_Token").setValue(MyFirebaseMessagingService.getToken(context))
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Toast.makeText(context, "User Created", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }

        database.addValueEventListener(postListener)


    }

    fun getUser(email: String, context: Context){
        tinyDB = TinyDB(context)
        Log.d("User_Email ","${email}")
        val separated: List<String> = email!!.split("@")
        val nodeName = separated[0]
        try {
            getUserImage(nodeName)

            database.child("User").child(nodeName).addListenerForSingleValueEvent(object :
                ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
//                for (snap in snapshot.getChildren()) {
//                    Log.d("REVIEWE_KEY_SNAP ","${snap}")
//                    Log.d("REVIEWE_KEY","${snap.key} ${snap.value}")
                    val user = snapshot.getValue(UserInfo::class.java)
                    userArrayList.add(user!!)
                    Log.d("USER_INFO ","${user.city}   ${user.gender}")
                    tinyDB.putString(K.USER_NAME, user!!.userName)
                    tinyDB.putString(K.GENDER,user.gender)
                    tinyDB.putString(K.CITY,user.city)


//                }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("ERROR_DATABASE","$error")
                }

            })
        }catch (e: Exception){
            Log.d("GET_USER_AUTH_VM ","${nodeName}")

        }


    }


    fun getUserImage(nodeName: String) {
        Log.d("IMAGE User ","${nodeName}")

        try {
            database.child("User").child(nodeName).child("Image")
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
//                for (snap in snapshot.getChildren()) {
                        val image = ""
                        Log.d("IMAGE User ", "${snapshot.key}   ${snapshot.value}")
                        val user = snapshot.getValue(UserImage::class.java)
                        Log.d("IMAGE User ", "${user!!.imgUrl}")
                        tinyDB.putString(K.USER_IMG, user!!.imgUrl)
//                    tinyDB.putString(K.USER_NAME, user!!.userName)

//                }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("ERROR_DATABASE", "$error")
                    }

                })
        }catch (e: Exception){
            Log.d("GET_IMAGE_EXCEPTION","${e.localizedMessage}")
        }

    }

}