package com.example.augmanium.afterAuth.profileFragment

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.augmanium.EditProfile
import com.example.augmanium.databinding.FragmentProfileBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
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

}