package com.example.augmanium.afterAuth.mainActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.search.searchDataClass.AllProductDataClass
import com.example.augmanium.databinding.ItemAllProductRvBinding
import com.example.augmanium.databinding.ItemSearchRvBinding

class AllProductViewAdapter(val allProductArrayList:ArrayList<AllProductDataClass>):
    RecyclerView.Adapter<AllProductViewAdapter.ViewHolderAllProduct>() {

    class ViewHolderAllProduct(val allProductRvBinding: ItemSearchRvBinding):RecyclerView.ViewHolder(allProductRvBinding.root) {

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAllProduct=
        ViewHolderAllProduct(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_search_rv,
            parent,
            false
        ))

    override fun onBindViewHolder(holder: ViewHolderAllProduct, position: Int) {
        holder.allProductRvBinding.dataClass = allProductArrayList[position]
    }

    override fun getItemCount(): Int {
        return allProductArrayList.size
    }
}