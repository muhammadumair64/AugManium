package com.example.augmanium.afterAuth.searchscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color

import android.text.SpannableString

import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.Spanned
import com.example.augmanium.R
import com.example.augmanium.databinding.SearchRecyclerViewBinding


class SearchAdapter(var productSearchArrayList: ArrayList<String>):
    RecyclerView.Adapter<SearchAdapter.searchViewHolder>(), Filterable{

var allProductArrayList:ArrayList<String> = ArrayList()
    var charactersLength=0
init {

    allProductArrayList.addAll(productSearchArrayList)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): searchViewHolder =
        searchViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.search_recycler_view,
                parent,
                false
            )
        )
    override fun onBindViewHolder(holder: searchViewHolder, position: Int) {
        holder.adapterViewBindingAdapter.searchedText.text = productSearchArrayList[position]
        var searchText=holder.adapterViewBindingAdapter.searchedText.text.toString()
        val spannable: Spannable = SpannableString(searchText)
        val fcsBlack = ForegroundColorSpan(Color.BLACK)
        spannable.setSpan(fcsBlack, 0, charactersLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.adapterViewBindingAdapter.searchedText.text=spannable

    }

    override fun getItemCount(): Int {
        return productSearchArrayList.size
    }

    class searchViewHolder(val adapterViewBindingAdapter: SearchRecyclerViewBinding)
        :RecyclerView.ViewHolder(adapterViewBindingAdapter.root){

    }

    override fun getFilter(): Filter {

  return myFilter
    }
    var myFilter: Filter = object : Filter() {
        //Automatic on background thread
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredList: ArrayList<String> = ArrayList()
            println("CharSequence${charSequence.length}")
            charactersLength=charSequence.length
            if (charSequence == null || charSequence.length == 0) {
                filteredList.addAll(allProductArrayList)
            } else {
                for (searches in allProductArrayList) {
                    if (searches.toString().toLowerCase().contains(charSequence.toString().toLowerCase())) {

                        filteredList.add(searches)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        //Automatic on UI thread
        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            productSearchArrayList.clear()
            productSearchArrayList.addAll(filterResults.values as Collection<String>)
            notifyDataSetChanged()
        }
    }


}