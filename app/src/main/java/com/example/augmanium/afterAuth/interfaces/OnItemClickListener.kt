package com.example.augmanium.afterAuth.interfaces

import android.view.View

interface OnItemClickListener {
    fun onClick(position: Int)
    fun moveToNextScreen(position: Int)
}