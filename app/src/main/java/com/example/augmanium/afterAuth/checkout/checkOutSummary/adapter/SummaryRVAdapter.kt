package com.example.augmanium.afterAuth.checkout.checkOutSummary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R

import com.example.augmanium.afterAuth.checkout.checkOutSummary.dataClass.SummaryDataClass
import com.example.augmanium.databinding.ItemSummaryProductListBinding

class SummaryRVAdapter(val summaryArrayList:ArrayList<SummaryDataClass>
): RecyclerView.Adapter<SummaryRVAdapter.SummaryRvViewHolder>() {

    class SummaryRvViewHolder(val summaryRvBinding:ItemSummaryProductListBinding ): RecyclerView.ViewHolder(summaryRvBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryRvViewHolder=
        SummaryRvViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_summary_product_list,
                parent,
                false
            ))

    override fun onBindViewHolder(holder: SummaryRvViewHolder, position: Int) {
        holder.summaryRvBinding.dataClass = summaryArrayList[position]

    }

    override fun getItemCount(): Int {
        return summaryArrayList.size
    }
}