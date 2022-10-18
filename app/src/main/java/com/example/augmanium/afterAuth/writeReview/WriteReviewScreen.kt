package com.example.augmanium.afterAuth.writeReview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.afterAuth.writeReview.wrightReviewViewModel.WriteReviewViewModel
import com.example.augmanium.databinding.ActivityWriteReviewScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteReviewScreen : AppCompatActivity() {

    lateinit var binding: ActivityWriteReviewScreenBinding
    val viewModel : WriteReviewViewModel  by viewModels<WriteReviewViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_write_review_screen)

        viewModel.viewBinding(binding, this)

        initViews()

    }


    fun initViews(){



    }
}