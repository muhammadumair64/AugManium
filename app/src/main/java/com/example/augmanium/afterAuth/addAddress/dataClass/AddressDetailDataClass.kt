package com.example.augmanium.afterAuth.addAddress.dataClass

data class AddressDetailDataClass(val home: String = "",
                                  val street: String = "",
                                  val name: String = "",
                                  val city: String = "",
                                  val state: String = "",
                                  var phNumber: String = "") {
}