package com.example.augmanium.afterAuth.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.notification.viewModel.NotificationViewModel
import com.example.augmanium.databinding.ActivityNotificationScreenBinding
import com.example.augmanium.utils.TinyDB
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationScreen : AppCompatActivity() {

    lateinit var binding: ActivityNotificationScreenBinding
    val viewModel: NotificationViewModel by viewModels<NotificationViewModel>()
    lateinit var tinyDb:TinyDB
   lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tinyDb=TinyDB(this)
        database = FirebaseDatabase.getInstance().reference
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification_screen)
        viewModel.notificationsRv(binding,this,database,tinyDb)
        binding.back.setOnClickListener {
            finish()
        }
    }


}