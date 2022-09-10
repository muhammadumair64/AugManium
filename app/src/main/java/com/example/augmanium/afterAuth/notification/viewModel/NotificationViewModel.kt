package com.example.augmanium.afterAuth.notification.viewModel

import androidx.lifecycle.ViewModel
import com.example.augmanium.afterAuth.notification.adapter.NotificationAdapter
import com.example.augmanium.afterAuth.notification.dataClass.NotificationDataClass
import com.example.augmanium.afterAuth.orderHistory.adapter.OrderHistoryAdapter
import com.example.augmanium.afterAuth.orderHistory.dataClass.OrderHistoryDataClass
import com.example.augmanium.databinding.ActivityNotificationScreenBinding
import com.example.augmanium.databinding.ActivityOrderHistoryBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(): ViewModel() {

    val notificationArrayList: ArrayList<NotificationDataClass> = ArrayList()

    fun notificationsRv(binding: ActivityNotificationScreenBinding) {
        notificationArrayList.add(NotificationDataClass("Alert!", "your parcel has been handed over to our overseas courier partner", "9 Jun , 2022 9:57 PM"))
        notificationArrayList.add(NotificationDataClass("Alert!", "your parcel has been handed over to our overseas courier partner", "9 Jun , 2022 9:57 PM"))
        notificationArrayList.add(NotificationDataClass("Alert!", "your parcel has been handed over to our overseas courier partner", "9 Jun , 2022 9:57 PM"))
        notificationArrayList.add(NotificationDataClass("Alert!", "your parcel has been handed over to our overseas courier partner", "9 Jun , 2022 9:57 PM"))
        notificationArrayList.add(NotificationDataClass("Alert!", "your parcel has been handed over to our overseas courier partner", "9 Jun , 2022 9:57 PM"))

        binding.orderHistoryRv.also {
            it.adapter = NotificationAdapter(notificationArrayList)
            it.setHasFixedSize(true)
        }
    }

}