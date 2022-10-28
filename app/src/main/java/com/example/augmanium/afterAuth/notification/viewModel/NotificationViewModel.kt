package com.example.augmanium.afterAuth.notification.viewModel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.augmanium.afterAuth.notification.adapter.NotificationAdapter
import com.example.augmanium.afterAuth.notification.dataClass.NotificationDataClass
import com.example.augmanium.databinding.ActivityNotificationScreenBinding
import com.example.augmanium.utils.AlertDataClass
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(): ViewModel() {

    val notificationArrayList: ArrayList<NotificationDataClass> = ArrayList()

    fun notificationsRv(
        binding: ActivityNotificationScreenBinding,
        context: Context,
        database: DatabaseReference,
        tinyDb: TinyDB
    ) {

        binding.progressLayout.visibility= View.VISIBLE

      var email =  tinyDb.getString(K.EMAIL)

        if (email != null) {
            email = email.split("@").toTypedArray()[0]
        }
        val postListener = object : ValueEventListener {


            override fun onDataChange(dataSnapshot: DataSnapshot) {
                println("DataTesting parent is ${dataSnapshot.key}  ")
                for (snapshot in dataSnapshot.children) {

                val node = snapshot.getValue(AlertDataClass::class.java)
                    if (node != null) {
                        notificationArrayList.add(NotificationDataClass("Alert!", node.body,
                            node.time
                        ))
                    }

                    Log.d("DataTesting","$snapshot")
                }
                binding.progressLayout.visibility=View.INVISIBLE

                binding.orderHistoryRv.also {
                    it.adapter = NotificationAdapter(notificationArrayList)
                    it.setHasFixedSize(true)

                }



            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DataTesting","Error : $error")

            }

        }

        if (email != null) {
            database.child("User").child(email).child("Alerts").addValueEventListener(postListener)
        }




    }






}