package com.example.augmanium.utils


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.augmanium.R
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MyFirebaseMessagingService:FirebaseMessagingService() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    val TAG = "FirebaseMessagingService"
    private lateinit var database: DatabaseReference
lateinit var tinyDB: TinyDB
companion object{
    fun getToken(context: Context): String? {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fcm_token", "empty")
    }
}


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token",p0).apply()
        Log.d("FCM_TOKEN_","$p0")
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {


    database = FirebaseDatabase.getInstance().reference
    tinyDB= TinyDB(this@MyFirebaseMessagingService)
    Log.d(TAG, "Push Notification: ${remoteMessage.from} body is ${remoteMessage.notification?.body}")

    if (remoteMessage.notification != null) {
        val email=  tinyDB.getString(K.EMAIL)
        Log.d(TAG, "$email")
        val body = remoteMessage.notification?.body
        val id = remoteMessage.messageId
        if (email != null) {
            uploadAlert(email,database,this@MyFirebaseMessagingService, body,id)
        }
        // Toast.makeText(this, ""+ remoteMessage.notification?.body, Toast.LENGTH_SHORT).show()
        showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
    }
}




//    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(title: String?, body: String?) {

//        val intent = Intent(this, SplashScreen::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "Default"
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        manager.notify(0, builder.build())


    }



    fun uploadAlert(
        email: String,
        database: DatabaseReference,
        context: Context,
        body: String?,
        id: String?
    ) {
        Log.d(TAG, "$email, $body , $id")
        val value = email.split("@").toTypedArray()[0]
        if (id != null) {
            val alertContent = body?.let { AlertDataClass(it,getTime()) }
            database.child("User").child(value).child("Alerts").child(id).setValue(alertContent)
        }
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


fun getTime(): String {
    val c : Calendar = Calendar.getInstance()
     var df : SimpleDateFormat? = null
  var formattedDate = ""

// goes to main method or onCreate(Android)
    df = SimpleDateFormat("dd-MM-yyyy HH:mm a")
    formattedDate = df!!.format(c.time)
    println("Format dateTime => $formattedDate")
    return formattedDate
}


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}