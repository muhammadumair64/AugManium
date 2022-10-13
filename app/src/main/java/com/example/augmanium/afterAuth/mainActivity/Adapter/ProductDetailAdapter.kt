package com.example.augmanium.afterAuth.mainActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.interfaces.OnItemClickListener
import com.example.augmanium.afterAuth.mainActivity.dataClass.AllProductDataClass
import com.example.augmanium.afterAuth.mainActivity.dataClass.ProductDetailCategoryProductDataClass
import com.example.augmanium.databinding.ItemProductDetailRvBinding

class ProductDetailAdapter(val productDetailArrayList:ArrayList<ProductDetailCategoryProductDataClass>
//, val moveToNextScreen: OnItemClickListener
):
    RecyclerView.Adapter<ProductDetailAdapter.ProductDetailViewHolder>() {

    class ProductDetailViewHolder(val itemCatagory: ItemProductDetailRvBinding): RecyclerView.ViewHolder(itemCatagory.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailViewHolder =
        ProductDetailViewHolder( DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_product_detail_rv,
            parent,
            false
        ))


    override fun onBindViewHolder(holder: ProductDetailViewHolder, position: Int) {
        holder.itemCatagory.dataClass = productDetailArrayList[position]

//        holder.itemView.setOnClickListener {
//            moveToNextScreen.onClick(position)
//        }
    }

    override fun getItemCount(): Int {
        return productDetailArrayList.size
    }
}