package com.example.augmanium.afterAuth.writeReview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.augmanium.R
import com.example.augmanium.afterAuth.writeReview.dataClass.WriteReviewDataClass
import com.example.augmanium.databinding.ItemWriteReviewRvBinding

class WriteReviewAdapter(val productReviewArrayList:ArrayList<WriteReviewDataClass>
//, val moveToNextScreen: OnItemClickListener
):
    RecyclerView.Adapter<WriteReviewAdapter.ProductReviewViewHolder>() {

    class ProductReviewViewHolder(val itemCatagory: ItemWriteReviewRvBinding) :
        RecyclerView.ViewHolder(itemCatagory.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductReviewViewHolder =
        ProductReviewViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_write_review_rv,
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