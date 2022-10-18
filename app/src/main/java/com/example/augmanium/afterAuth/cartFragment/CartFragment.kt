package com.example.augmanium.afterAuth.cartFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.augmanium.R
import com.example.augmanium.afterAuth.mainActivity.MainActivity
import com.example.augmanium.databinding.FragmentCartBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {


    lateinit var binding: FragmentCartBinding
    val viewModel: CartFragmentViewModel by viewModels<CartFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)

        val viewBinding = binding.root
        val activityContext = (activity as MainActivity).context
binding.menuDrawer.setOnClickListener {
    (activity as MainActivity).menuFuction()
}

        viewModel.bindView(binding,activityContext)

        return viewBinding
    }

}