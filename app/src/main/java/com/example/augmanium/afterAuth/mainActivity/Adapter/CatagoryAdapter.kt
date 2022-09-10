package com.example.augmanium.afterAuth.mainActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.search.searchDataClass.CatagoryDataClass
import com.example.augmanium.databinding.ItemCategoryRvBinding

class CatagoryAdapter (val catagoryArrayList:ArrayList<CatagoryDataClass>):
    RecyclerView.Adapter<CatagoryAdapter.ItemCatagoryViewHolder>() {

    class ItemCatagoryViewHolder(val itemCatagory: ItemCategoryRvBinding):RecyclerView.ViewHolder(itemCatagory.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCatagoryViewHolder =
        ItemCatagoryViewHolder( DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_category_rv,
            parent,
            false
        ))


    override fun onBindViewHolder(holder: ItemCatagoryViewHolder, position: Int) {
        holder.itemCatagory.dataClass = catagoryArrayList[position]
    }

    override fun getItemCount(): Int {
        return catagoryArrayList.size
    }
}