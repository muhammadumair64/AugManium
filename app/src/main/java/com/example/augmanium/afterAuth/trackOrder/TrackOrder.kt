package com.example.augmanium.afterAuth.trackOrder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.checkout.checkOutSummary.dataClass.StatusDataClass
import com.example.augmanium.afterAuth.location.Location
import com.example.augmanium.afterAuth.mainActivity.MainActivity
import com.example.augmanium.databinding.ActivityTrackOrderBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.*

class TrackOrder : AppCompatActivity() {
    lateinit var tinyDb:TinyDB
    lateinit var binding: ActivityTrackOrderBinding
    lateinit var database: DatabaseReference
    var orderID = ""
    var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_track_order)
        tinyDb = TinyDB(this)
        database = FirebaseDatabase.getInstance().reference
        try{
            getOrderStatus()
        } catch (e:Exception){
            Log.d("TrackOrderTag","Error : $e")
        }

        btnListners()
    }

    private fun btnListners() {

        binding.backButtonproductDetails.setOnClickListener {
            finish()
        }

        binding.ContinueBtn.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    fun getOrderStatus()
    {
        try{
             email = tinyDb.getString(K.EMAIL).toString()
            email = email.split("@").toTypedArray()[0]
            orderID = tinyDb.getString(K.Order).toString()
            if(orderID==""){
                binding.trackOrderBtn.visibility = View.INVISIBLE
                Toast.makeText(this, "Order Not place yet", Toast.LENGTH_SHORT).show()
            }
            binding.OrderID.text = orderID.toString()


        } catch (e:Exception){
            Log.d("TrackOrderTag","Error : $e")
        }


        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                println("Data Of status $dataSnapshot")

                val order = dataSnapshot.getValue(StatusDataClass::class.java)

                  orderStatus(order,orderID)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }


        if (email != null) {
            if (orderID != null) {
                database.child("User").child(email).child("Orders").child(orderID).child("status")
                    .addValueEventListener(postListener)
            }
        }

    }




     fun orderStatus(order: StatusDataClass?, orderID: String?) {

    if (order != null) {
        binding.OrderID.text = orderID

        if(order.phase1 != ""){
            binding.phase1Text.text= order.phase1
            binding.phase1Dot.visibility= View.VISIBLE
        }
        if(order.phase2 != ""){
            binding.phase2Text.text= order.phase2
            binding.phase2Dot.visibility= View.VISIBLE
        }
        if(order.phase3 != ""){
            binding.phase3Text.text= order.phase3
            binding.phase3Dot.visibility= View.VISIBLE
        }
        if(order.phase4 != ""){
            binding.phase4Text.text= order.phase4
            binding.phase4Dot.visibility= View.VISIBLE
        }
        if(order.phase5 != ""){
            binding.phase5Text.text= order.phase5
            binding.phase5Dot.visibility= View.VISIBLE
        }
    }

binding.trackOrderBtn.setOnClickListener {
    val intent = Intent(this, Location::class.java)
    startActivity(intent)
}


}

}