package com.example.augmanium.afterAuth.mainActivity


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.augmanium.R
import com.example.augmanium.afterAuth.mainActivity.viewModel.MainActivityViewModel
import com.example.augmanium.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
//    val viewModel: MainActivityViewModel by viewModels<MainActivityViewModel>()
    val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

     navigationBar()

//        viewModel.catagoryRvBinding(this,binding)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.navigationbar_menu, menu)
//        layoutInflater.setFactory { _, _, attrs ->
//            Log.d("NAV_BAR", "Called")
//            (menu.getItem(R.id.Home) as com.ismaeldivita.chipnavigation.model.MenuItem).backgroundColor =
//                Color.parseColor("#FFFFFFFF")
//            null
//        }
//        return super.onCreateOptionsMenu(menu)
//    }



    fun navigationBar() {

        binding.menu.setMenuResource(
            R.menu.navigationbar_menu,
            Color.parseColor("#FFFFFFFF")
        )

        binding.menu.setItemSelected(R.id.home, true)

//        binding.menu.setup
        binding.menu.setOnItemSelectedListener {

            when (it) {
                R.id.homeFragment -> {
                    Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.profileFragment -> {
                    println("clicked")


                }
                R.id.cartFragment -> {


                }

            }


        }
    }
}