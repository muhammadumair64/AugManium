package com.example.augmanium.afterAuth.checkout

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.addAddress.AddAddress
import com.example.augmanium.afterAuth.addAddress.dataClass.AddressDetailDataClass
import com.example.augmanium.afterAuth.checkout.checkOutPayment.CheckOutPayment
import com.example.augmanium.databinding.ActivityCheckOutAddressBinding

import com.example.augmanium.utils.K
import com.example.augmanium.utils.MyFirebaseMessagingService
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CheckOutAddress : AppCompatActivity() {
    lateinit var binding: ActivityCheckOutAddressBinding
    lateinit var tinyDB: TinyDB
    lateinit var signInInputsArray: Array<EditText>

    lateinit var home: String
    lateinit var street: String
    lateinit var name: String
    lateinit var city: String
    lateinit var state: String
    lateinit var number: String

    var context :Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tinyDB = TinyDB(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_out_address)

        signInInputsArray = arrayOf(binding.home, binding.streetDetail, binding.nameEditText, binding.cityNameEditText, binding.stateNameEditText, binding.numberEditText)
        val tokan = MyFirebaseMessagingService.getToken(this)
        if (tokan != null) {
            Log.d("AddressTesting",tokan)
        }
        getDataFunction()
    }


    fun getDataFunction() {

        val email = tinyDB.getString(K.EMAIL)
        val separated: List<String> = email!!.split("@")
        val nodeName = separated[0]

        getUserAddress(nodeName)

        binding.applyBtn.setOnClickListener {
            binding.apply {

                checkDetails(nodeName)
            }


        }
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.cancelButton.setOnClickListener {
            finish()
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
                    binding.progressLayout.visibility=View.INVISIBLE
                    Toast.makeText(context, "UPLOADED", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context , CheckOutPayment::class.java)
                    startActivity(intent)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message

                }
            }

            rootRef.addValueEventListener(postListener)



//        val userinfo = AddressDetailDataClass(home,street,name,city,state,number)
//        Log.d("Data_FIRE", "${name} $city")
//        val rootRef = FirebaseDatabase.getInstance().reference
//        val yourRef = rootRef.child("User").child(nodeName).child("User Address")
//        yourRef.setValue(userinfo)
//
//        Toast.makeText(context,"UPLOADED", Toast.LENGTH_SHORT).show()



//        if (tinyDB.getInt(K.SIGN_UP) == 1){
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }else{
//            finish()
//        }
//        var intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)


    }

    fun getUserAddress(nodeName: String) {
        Log.d("IMAGE User ","${nodeName}")
        val database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference
        database.child("User").child(nodeName).child("User Address").addListenerForSingleValueEvent(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
//                for (snap in snapshot.getChildren()) {

                Log.d("IMAGE User ","${snapshot.key}   ${snapshot.value}")
                val user = snapshot.getValue(AddressDetailDataClass::class.java)
                Log.d("IMAGE User ","${user}")

//                binding.password.setText(password, TextView.BufferType.EDITABLE)
                if (user != null) {
                    binding.home.setText(user.home, TextView.BufferType.EDITABLE)
                    binding.streetDetail.setText(user.street, TextView.BufferType.EDITABLE)
                    binding.nameEditText.setText(user.name, TextView.BufferType.EDITABLE)
                    binding.cityNameEditText.setText(user.city, TextView.BufferType.EDITABLE)
                    binding.stateNameEditText.setText(user.state, TextView.BufferType.EDITABLE)
                    binding.numberEditText.setText(user.phNumber, TextView.BufferType.EDITABLE)


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
                val value = "${home.text}:${streetDetail.text},${nameEditText.text},${cityNameEditText.text},${stateNameEditText.text}"
                tinyDB.putString(K.ADDRESS,value)
                tinyDB.putString(K.ADDRESS, value)
                binding.progressLayout.visibility= View.VISIBLE
                uploadDataToFireBase(nodeName)
                Log.d("AddressTesting","$value")
            }
        }


    }

    fun showToast(toastText: String) {
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
    }



}