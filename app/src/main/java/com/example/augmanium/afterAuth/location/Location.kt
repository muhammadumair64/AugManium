package com.example.augmanium.afterAuth.location

import android.content.res.Resources
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.augmanium.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import java.util.*

class Location : AppCompatActivity() {

    lateinit var placeName: TextView
    lateinit var button: TextView
    lateinit var mapFragment: SupportMapFragment
    private lateinit var callback: OnMapReadyCallback
    lateinit var databaseReference: DatabaseReference
    var latitude = 0.0
    var longitude = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        initViews()
        getLocationFromFireBase()
        initListiner()
    }


    private fun initViews() {
        databaseReference = FirebaseDatabase.getInstance().reference
        placeName = findViewById(R.id.location)
        button = findViewById(R.id.backbutton)
        mapFragment =
            supportFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment
    }

    fun initListiner() {

        button.setOnClickListener {

            finish()
        }
    }

    fun mapCallback() {


        callback = OnMapReadyCallback { googleMap ->
            try {
                val success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.night_map
                    )
                )
                println("123Location $latitude ")
                val my_lahore = LatLng(latitude, longitude)
                googleMap.addMarker(
                    MarkerOptions().position(my_lahore).title("Marker in my Location")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(my_lahore))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_lahore, 20.0f))
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                println("locTION ${addresses[0].featureName}")

                placeName.text = addresses[0].featureName.toString()
                if (!success) {
                    System.out.println("Style parsing failed.")
                }
                println("location112233 $my_lahore")

            } catch (e: Resources.NotFoundException) {
                println("Can't find style. Error: ")

            }


        }
        mapFragment!!.getMapAsync(callback)

    }


    fun getLocationFromFireBase() {

        val optionselect: String? = intent.getStringExtra("option")
        val mobname: String? = intent.getStringExtra("device name")

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                println("Data Of Location $dataSnapshot")

                val latlong = dataSnapshot.getValue(LatLongDataClass::class.java)
                latitude = latlong!!.latitude
                longitude = latlong!!.longitude
                println(latlong)
                if (latlong != null) {
                    mapCallback()
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        databaseReference.child("User").child("arbazejaz0316").child("Orders").child("11223344").child("location")
            .addValueEventListener(postListener)


    }
}