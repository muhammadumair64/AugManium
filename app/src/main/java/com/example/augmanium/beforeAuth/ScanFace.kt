package com.example.augmanium.beforeAuth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.beforeAuth.viewModel.ViewModelScanFace
import com.example.augmanium.databinding.ActivityScanFaceBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ScanFace : AppCompatActivity() {

    lateinit var binding: ActivityScanFaceBinding
    val viewModel: ViewModelScanFace by viewModels<ViewModelScanFace>()
    var timerExecutor = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_scan_face)

        viewModel.initViews(binding, this)
    }
}