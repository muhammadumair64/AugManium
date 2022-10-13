package com.example.augmanium.afterAuth.searchscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.databinding.ItemFlexboxRvBinding
import com.example.augmanium.afterAuth.searchscreen.dataclass.FlexBoxDataClass

class FlexBoxAdapter (var arrayList: ArrayList<FlexBoxDataClass>):
    RecyclerView.Adapter<FlexBoxAdapter.flexboxViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): flexboxViewHolder =
        flexboxViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_flexbox_rv,
                parent,
                false
            )
        )
    override fun onBindViewHolder(holder: flexboxViewHolder, position: Int) {
        holder.flexBoxBindingAdapter.flextext = arrayList[position]
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class flexboxViewHolder(val flexBoxBindingAdapter: ItemFlexboxRvBinding)
        : RecyclerView.ViewHolder(flexBoxBindingAdapter.root){

    }
}