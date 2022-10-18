package com.example.augmanium.afterAuth.profileFragment

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.augmanium.EditProfile
import com.example.augmanium.TrackOrder
import com.example.augmanium.afterAuth.addAddress.AddAddress
import com.example.augmanium.afterAuth.mainActivity.MainActivity
import com.example.augmanium.afterAuth.orderHistory.OrderHistory
import com.example.augmanium.afterAuth.searchscreen.SearchActivity
import com.example.augmanium.beforeAuth.SplashScreen
import com.example.augmanium.databinding.FragmentProfileBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor() :ViewModel() {

    lateinit var binding: FragmentProfileBinding
    lateinit var context: Context
    lateinit var tinyDB: TinyDB

    fun bindView(activityContext: Context, activityBinding: FragmentProfileBinding){
        binding = activityBinding
        context = activityContext
        tinyDB = TinyDB(context)



        setData()
        listners()

    }

    private fun listners() {
        binding.editProfile.setOnClickListener {
            var intent = Intent(context, EditProfile::class.java)
            tinyDB.putInt(K.SIGN_UP,2)
            context.startActivity(intent)

        }

        binding.searchBtn.setOnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
        binding.orderHistory.setOnClickListener {
            val intent = Intent(context, OrderHistory::class.java)
            context.startActivity(intent)
        }
        binding.trackOrderBtn.setOnClickListener {
            val intent = Intent(context, TrackOrder::class.java)
            context.startActivity(intent)
        }
        binding.shippingAdress.setOnClickListener {
            val intent = Intent(context, AddAddress::class.java)
            context.startActivity(intent)
        }
        binding.logOut.setOnClickListener {
            logOut()
        }
    }

    fun setData(){
        var userName = tinyDB.getString(K.USER_NAME)
        var userEmail = tinyDB.getString(K.EMAIL)
        var userImage = tinyDB.getString(K.USER_IMG)
        binding.Username.text = userName
        binding.email.text = userEmail
        Glide.with(context)
            .load(userImage)
            .override(300, 200)
            .into(binding.profilePhoto);
    }

    fun logOut() {
        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener { // user is now signed out
//                    startActivity(Intent(this@MyActivity, SignInActivity::class.java))
//                    finish()
                Toast.makeText(context,"Sign out done", Toast.LENGTH_SHORT).show()
                tinyDB.clear()

                var intent = Intent(context, SplashScreen::class.java)
                context.startActivity(intent)
                (context as MainActivity).finish()
                Log.d("signout ","Done")

            }.addOnFailureListener {
                Log.d("Faild_to_signout"," ${it.localizedMessage}")
            }
    }

}