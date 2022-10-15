package com.example.augmanium.afterAuth.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.category.dataClass.AllCategoryDataClass
import com.example.augmanium.afterAuth.interfaces.OnItemClickListener
import com.example.augmanium.databinding.ItemAllCategoryRvBinding
import com.example.augmanium.databinding.ItemCategoryProductsBinding

class AllCategoriesAdapter (val allCategoryArrayList:ArrayList<AllCategoryDataClass>, val nextScreen: OnItemClickListener):
    RecyclerView.Adapter<AllCategoriesAdapter.AllCatagoryViewHolder>() {

    class AllCatagoryViewHolder(val allCatagoryBinding: ItemAllCategoryRvBinding): RecyclerView.ViewHolder(allCatagoryBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCatagoryViewHolder =
        AllCatagoryViewHolder( DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_all_category_rv,
            parent,
            false
        ))


    override fun onBindViewHolder(holder: AllCatagoryViewHolder, position: Int) {
        holder.allCatagoryBinding.dataClass = allCategoryArrayList[position]
        holder.itemView.setOnClickListener {
            nextScreen.moveToNextScreen(position)
        }
    }

    override fun getItemCount(): Int {
        return allCategoryArrayList.size
    }
}