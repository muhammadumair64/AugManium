package com.example.augmanium.afterAuth.mainActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.searchResult.searchDataClass.AllProductDataClass
import com.example.augmanium.databinding.ItemAllProductRvBinding

class AllProductAdapter(val allProductArrayList:ArrayList<AllProductDataClass>):
    RecyclerView.Adapter<AllProductAdapter.AllProductViewHolder>() {

    class AllProductViewHolder(val allProductRvBinding: ItemAllProductRvBinding):RecyclerView.ViewHolder(allProductRvBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProductViewHolder=
        AllProductViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_all_product_rv,
            parent,
            false
        ))

    override fun onBindViewHolder(holder: AllProductViewHolder, position: Int) {
        holder.allProductRvBinding.dataClass = allProductArrayList[position]
    }

    override fun getItemCount(): Int {
        return allProductArrayList.size
    }
}