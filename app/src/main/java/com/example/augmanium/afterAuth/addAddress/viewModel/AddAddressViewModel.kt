package com.example.augmanium.afterAuth.addAddress.viewModel

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.augmanium.afterAuth.addAddress.AddAddress
import com.example.augmanium.afterAuth.addAddress.dataClass.AddressDetailDataClass
import com.example.augmanium.databinding.ActivityAddAddressBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor() : ViewModel() {

    lateinit var context: Context
    lateinit var binding: ActivityAddAddressBinding
    lateinit var tinyDB : TinyDB
    lateinit var signInInputsArray: Array<EditText>
    lateinit var home: String
    lateinit var street: String
    lateinit var name: String
    lateinit var city: String
    lateinit var state: String
    lateinit var number: String

    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference

    fun binding(activityContext: Context, activityBinding: ActivityAddAddressBinding){
        context = activityContext
        binding = activityBinding
        tinyDB = TinyDB(context)
        val email = tinyDB.getString(K.EMAIL)
        val separated: List<String> = email!!.split("@")
        val nodeName = separated[0]

        signInInputsArray = arrayOf(binding.home, binding.streetDetail, binding.nameEditText, binding.cityNameEditText, binding.stateNameEditText, binding.numberEditText)

        getUserAddress(nodeName)

        binding.saveBtn.setOnClickListener {
            checkDetails(nodeName)

        }

        binding.backButton.setOnClickListener {
            (context as AddAddress).finish()
        }

    }


    private fun notEmpty(): Boolean = home.isNotEmpty() && street.isNotEmpty() && name.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty() && number.isNotEmpty()
    fun uploadDataToFireBase(nodeName: String) {

        val userinfo = AddressDetailDataClass(home, street, name, city, state, number)
        Log.d("Data_FIRE", "${name} $city")
        val rootRef = FirebaseDatabase.getInstance().reference
        val yourRef = rootRef.child("User").child(nodeName).child("User Address")
        yourRef.setValue(userinfo)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Toast.makeText(context, "UPLOADED", Toast.LENGTH_SHORT).show()
                binding.progressLayout.visibility=View.INVISIBLE
                (context as AddAddress).finish()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }

        rootRef.addValueEventListener(postListener)

    }


    fun getUserAddress(nodeName: String) {
        Log.d("IMAGE User ","${nodeName}")
        var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference
        database.child("User").child(nodeName).child("User Address").addListenerForSingleValueEvent(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
//                for (snap in snapshot.getChildren()) {

                Log.d("IMAGE User ","${snapshot.key}   ${snapshot.value}")
                val user = snapshot.getValue(AddressDetailDataClass::class.java)
                    Log.d("IMAGE User ","${user}")

//                binding.password.setText(password, TextView.BufferType.EDITABLE)
                if (user != null) {

                    binding.home.setText(user!!.home, TextView.BufferType.EDITABLE)
                    binding.streetDetail.setText(user!!.street, TextView.BufferType.EDITABLE)
                    binding.nameEditText.setText(user!!.name, TextView.BufferType.EDITABLE)
                    binding.cityNameEditText.setText(user!!.city, TextView.BufferType.EDITABLE)
                    binding.stateNameEditText.setText(user!!.state, TextView.BufferType.EDITABLE)
                    binding.numberEditText.setText(user!!.phNumber, TextView.BufferType.EDITABLE)

                }


//                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")
            }

        })
    }


    fun checkDetails(nodeName: String) {
        home = binding.home.text.toString().trim()
        street = binding.streetDetail.text.toString().trim()
        name = binding.nameEditText.text.toString().trim()
        city = binding.cityNameEditText.text.toString().trim()
        state = binding.stateNameEditText.text.toString().trim()
        number = binding.numberEditText.text.toString().trim()
        if (notEmpty()){
            validateText(nodeName)
        }else{
            signInInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }
    }

    fun validateText(nodeName: String) {
        binding.apply {
            if(home.text.length <= 2)
            {
                showToast("Incomplete Home Address")
            }else if(streetDetail.text.length <= 5){
                showToast("Incomplete Street Detail")
            }else if(nameEditText.text.length <= 3){
                showToast("Incomplete Name")
            }else if(cityNameEditText.text.length <= 3){
                showToast("Incomplete City Name")
            }else if(stateNameEditText.text.length <= 3){
                showToast("Incomplete State Name")
            }else if(numberEditText.text.length <= 10){
                showToast("Incomplete Number")
            }else{
                binding.progressLayout.visibility= View.VISIBLE
                uploadDataToFireBase(nodeName)
            }
        }


    }

    fun showToast(toastText: String) {
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
    }

}