package com.example.augmanium.afterAuth.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.category.dataClass.SpecificCatagoryProductDataClass
import com.example.augmanium.afterAuth.interfaces.OnItemClickListener
import com.example.augmanium.databinding.ItemCategoryProductsBinding
import com.example.augmanium.databinding.ItemCategoryRvBinding

class SpecificCategoryProductAdapter(val productNameArrayList:ArrayList<SpecificCatagoryProductDataClass>,val  onItemClick: OnItemClickListener):
    RecyclerView.Adapter<SpecificCategoryProductAdapter.ItemCatagoryViewHolder>() {

    class ItemCatagoryViewHolder(val itemCatagoryProduct: ItemCategoryProductsBinding): RecyclerView.ViewHolder(itemCatagoryProduct.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCatagoryViewHolder =
        ItemCatagoryViewHolder( DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_category_products,
            parent,
            false
        ))


    override fun onBindViewHolder(holder: ItemCatagoryViewHolder, position: Int) {
        holder.itemCatagoryProduct.dataClass = productNameArrayList[position]

        holder.itemView.setOnClickListener {
            onItemClick.moveToNextScreen(position)
        }
    }

    override fun getItemCount(): Int {
        return productNameArrayList.size
    }
}