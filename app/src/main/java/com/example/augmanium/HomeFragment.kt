package com.example.augmanium

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.augmanium.afterAuth.mainActivity.MainActivity
import com.example.augmanium.afterAuth.mainActivity.viewModel.MainActivityViewModel
import com.example.augmanium.databinding.FragmentHomeBinding
import com.google.firebase.database.DatabaseReference


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    val viewModel: MainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activityContext = (activity as MainActivity).context
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel.catagoryRvBinding(activityContext, binding)


        return binding.root
    }

}