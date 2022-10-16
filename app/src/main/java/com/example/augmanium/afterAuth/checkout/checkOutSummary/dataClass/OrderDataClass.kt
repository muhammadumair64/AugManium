package com.example.augmanium.afterAuth.checkout.checkOutSummary.dataClass

data class OrderDataClass(var totalPrice:Int =0 , var OrderID:String= "" , var time :String = "",  var summaryArrayList: ArrayList<SummaryDataClass> = ArrayList() ) {
}