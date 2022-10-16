package com.example.augmanium.afterAuth.orderHistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.cartFragment.interfaceForClick.CartRVClick
import com.example.augmanium.afterAuth.orderHistory.dataClass.OrderHistoryDataClass
import com.example.augmanium.afterAuth.orderHistory.orderInterface.OnItemClicked
import com.example.augmanium.databinding.ItemOrderHistoryRvBinding
import com.example.augmanium.databinding.ItemSearchRvBinding

class OrderHistoryAdapter(val orderHistoryArrayList:ArrayList<OrderHistoryDataClass>,val orderInterface: OnItemClicked):
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolderOrderHistory>() {

    class ViewHolderOrderHistory(val orderHistoryRvBinding: ItemOrderHistoryRvBinding): RecyclerView.ViewHolder(orderHistoryRvBinding.root) {

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOrderHistory=
        ViewHolderOrderHistory(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_order_history_rv,
            parent,
            false
        ))

    override fun onBindViewHolder(holder: ViewHolderOrderHistory, position: Int) {
        holder.orderHistoryRvBinding.dataClass = orderHistoryArrayList[position]
        holder.itemView.setOnClickListener {
            orderInterface.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return orderHistoryArrayList.size
    }
}