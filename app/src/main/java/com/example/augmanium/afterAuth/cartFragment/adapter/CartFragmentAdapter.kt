package com.example.augmanium.afterAuth.cartFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.cartFragment.dataClass.CartFragmentDataClass
import com.example.augmanium.afterAuth.cartFragment.interfaceForClick.CartRVClick
import com.example.augmanium.databinding.ItemCartProductBinding
import kotlinx.android.synthetic.main.item_cart_product.view.*

class CartFragmentAdapter(val cartFragmentArrayList:ArrayList<CartFragmentDataClass>, val cartInterface: CartRVClick
): RecyclerView.Adapter<CartFragmentAdapter.CartFragmentViewHolder>() {

    class CartFragmentViewHolder(val allProductRvBinding: ItemCartProductBinding): RecyclerView.ViewHolder(allProductRvBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartFragmentViewHolder=
        CartFragmentViewHolder(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_cart_product,
            parent,
            false
        ))

    override fun onBindViewHolder(holder: CartFragmentViewHolder, position: Int) {
        holder.allProductRvBinding.dataClass = cartFragmentArrayList[position]
        holder.allProductRvBinding.deleteBtn.setOnClickListener {
            cartInterface.onDlt(position)
        }

    }

    override fun getItemCount(): Int {
        return cartFragmentArrayList.size
    }
}