package com.example.augmanium.afterAuth.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.notification.viewModel.NotificationViewModel
import com.example.augmanium.databinding.ActivityNotificationScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationScreen : AppCompatActivity() {

    lateinit var binding: ActivityNotificationScreenBinding
    val viewModel: NotificationViewModel by viewModels<NotificationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification_screen)
        viewModel.notificationsRv(binding)
    }


}