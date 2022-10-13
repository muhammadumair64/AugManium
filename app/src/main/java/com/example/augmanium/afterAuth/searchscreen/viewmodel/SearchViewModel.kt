package com.example.augmanium.afterAuth.searchscreen.viewmodel

import android.content.Context
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.*
import com.example.augmanium.R
import com.example.augmanium.databinding.ActivitySearchBinding
import com.example.augmanium.afterAuth.searchscreen.adapter.FlexBoxAdapter
import com.example.augmanium.afterAuth.searchscreen.adapter.SearchAdapter
import com.example.augmanium.afterAuth.searchscreen.dataclass.FlexBoxDataClass
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor():ViewModel() {


    var arrayListMuteableLiveData = MutableLiveData<ArrayList<String>>()
    var searchedArrayList = ArrayList<String>()

    var flexMutableLiveData = MutableLiveData<ArrayList<FlexBoxDataClass>>()
    var flexArrayList = ArrayList<FlexBoxDataClass>()
    lateinit var searchAdapter: SearchAdapter




    fun getArrayList() {
        searchedArrayList.add("bucci white dress")
        searchedArrayList.add("Gucci watch")
        searchedArrayList.add("Gucci shoes")
        searchedArrayList.add("Gucci white dress")
        searchedArrayList.add("Armani watch")
        searchedArrayList.add("Iucci shoes")
        searchedArrayList.add("Jucci white dress")
        searchedArrayList.add("Fcci watch")
        searchedArrayList.add("wucci shoes")
        searchedArrayList.add("qucci white dress")



        arrayListMuteableLiveData.value = searchedArrayList


    }

    fun getArrayListFlexBox(): MutableLiveData<ArrayList<FlexBoxDataClass>> {
        flexArrayList.add(FlexBoxDataClass("Gucci white dress"))
        flexArrayList.add(FlexBoxDataClass("Gucci watch"))
        flexArrayList.add(FlexBoxDataClass("Gucci shoes"))
        flexArrayList.add(FlexBoxDataClass("Gucci watch"))
        flexArrayList.add(FlexBoxDataClass("Gucci shoes"))
        flexArrayList.add(FlexBoxDataClass("Gucci watch"))
        flexArrayList.add(FlexBoxDataClass("Gucci shoes"))

        flexMutableLiveData.value = flexArrayList

        return flexMutableLiveData
    }

    fun bindingSearchScreen(binding:ActivitySearchBinding ,context: Context)
    {

//            binding.searchRecyclerview.also {
//                searchAdapter= SearchAdapter(searchedArrayList)
////                it.adapter=SearchAdapter(searchedArrayList)
//                it.layoutManager=LinearLayoutManager(context)
//                it.setHasFixedSize(true)
//            }


        val typeface = ResourcesCompat.getFont(context, R.font.montserrat_regular)
        val id: Int = binding.searchText.getContext().getResources()
            .getIdentifier("android:id/search_src_text", null, null)
        val textView =  binding.searchText.findViewById(id) as TextView
        textView.typeface = typeface

        getArrayListFlexBox()
        binding.flexboxRv.also {
            it.adapter = FlexBoxAdapter(flexArrayList)
            it.layoutManager = FlexboxLayoutManager(context)
            (it.layoutManager as FlexboxLayoutManager).flexDirection = FlexDirection.ROW
            (it.layoutManager as FlexboxLayoutManager).justifyContent = JustifyContent.FLEX_START
            (it.layoutManager as FlexboxLayoutManager).alignItems = AlignItems.FLEX_START
            it.setHasFixedSize(true)





        }
    }
}