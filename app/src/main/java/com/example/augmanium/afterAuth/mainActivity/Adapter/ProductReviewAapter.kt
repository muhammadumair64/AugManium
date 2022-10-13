package com.example.augmanium.afterAuth.mainActivity.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.mainActivity.dataClass.ReviewDataclass
import com.example.augmanium.databinding.ItemProductDetailRvBinding
import com.example.augmanium.databinding.ItemProductReviewRvBinding

class ProductReviewAapter (val productReviewArrayList:ArrayList<ReviewDataclass>
//, val moveToNextScreen: OnItemClickListener
):
    RecyclerView.Adapter<ProductReviewAapter.ProductReviewViewHolder>() {

    class ProductReviewViewHolder(val itemCatagory: ItemProductReviewRvBinding) :
        RecyclerView.ViewHolder(itemCatagory.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductReviewViewHolder =
        ProductReviewViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_product_review_rv,
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ProductReviewViewHolder, position: Int) {
        holder.itemCatagory.database = productReviewArrayList[position]

//        holder.itemView.setOnClickListener {
//            moveToNextScreen.onClick(position)
//        }
    }

    override fun getItemCount(): Int {
        return productReviewArrayList.size
    }
}