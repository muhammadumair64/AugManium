package com.example.augmanium.afterAuth.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.notification.dataClass.NotificationDataClass
import com.example.augmanium.databinding.ItemNotificationRvBinding
import com.example.augmanium.databinding.ItemSearchRvBinding

class NotificationAdapter(val notificationArrayList:ArrayList<NotificationDataClass>):
    RecyclerView.Adapter<NotificationAdapter.ViewHolderNotification>() {

    class ViewHolderNotification(val notificationRvBinding: ItemNotificationRvBinding): RecyclerView.ViewHolder(notificationRvBinding.root) {

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNotification=
        ViewHolderNotification(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_notification_rv,
            parent,
            false
        ))

    override fun onBindViewHolder(holder: ViewHolderNotification, position: Int) {
        holder.notificationRvBinding.dataClass = notificationArrayList[position]
    }

    override fun getItemCount(): Int {
        return notificationArrayList.size
    }
}