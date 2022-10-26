package com.example.augmanium.afterAuth.mainActivity


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.augmanium.R
import com.example.augmanium.afterAuth.mainActivity.viewModel.MainActivityViewModel
import com.example.augmanium.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.augmanium.afterAuth.category.AllCategories
import com.example.augmanium.afterAuth.category.CategoryScreen
import com.example.augmanium.afterAuth.notification.NotificationScreen
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import org.checkerframework.checker.units.qual.A
import java.util.*
import kotlin.concurrent.schedule


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
companion object{

    var mainContext = this
}
 lateinit var tinyDB: TinyDB
    var position = 0
    lateinit var binding:ActivityMainBinding

//    val viewModel: MainActivityViewModel by viewModels<MainActivityViewModel>()
    val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        tinyDB = TinyDB(this)
        slidingDrawer()
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

        binding.menu.setItemSelected(R.id.homeFragment, true)

//        binding.menu.setup
        try{
            binding.menu.setOnItemSelectedListener {

                when (it) {

                    R.id.homeFragment -> {

                        when(position){
                            2->{
                                findNavController(R.id.navHostFragment).navigate(
                                    R.id.action_cartFragment_to_homeFragment, null, null
                                )
                                position=0
                            }
                            3->{
                                findNavController(R.id.navHostFragment).navigate(
                                    R.id.action_profileFragment_to_homeFragment, null, null
                                )
                                position=0
                            }
                            4->{
                                findNavController(R.id.navHostFragment).navigate(
                                    R.id.action_settingsFragment_to_homeFragment, null, null
                                )
                                position=0
                            }

                        }

                    }
                    R.id.profileFragment -> {

                        when(position){
                            0->{
                                findNavController(R.id.navHostFragment).navigate(
                                    R.id.action_homeFragment_to_profileFragment, null, null
                                )
                                position=3
                            }
                            2->{
                                findNavController(R.id.navHostFragment).navigate(
                                    R.id.action_cartFragment_to_profileFragment, null, null
                                )
                                position=3
                            }
                            4->{
                                findNavController(R.id.navHostFragment).navigate(
                                    R.id.action_settingsFragment_to_profileFragment, null, null
                                )
                                position=3
                            }

                        }


                    }
                    R.id.cartFragment -> {

                        when(position){
                            0->{
                                findNavController(R.id.navHostFragment).navigate(
                                    R.id.action_homeFragment_to_cartFragment, null, null

                                )
                                position = 2
                            }
                            3->{
                                findNavController(R.id.navHostFragment).navigate(
                                    R.id.action_profileFragment_to_cartFragment, null, null
                                )
                                position = 2
                            }
                            4->{
                                findNavController(R.id.navHostFragment).navigate(
                                    R.id.action_settingsFragment_to_cartFragment, null, null
                                )
                                position = 2
                            }

                        }

                    }



                }


            }
        } catch (e:Exception){
            Log.d("NavigationBar","Error : $e")
        }

    }




    fun slidingDrawer(){

        binding.naviView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()

                }
                R.id.action_catalog -> {
                    val intent = Intent(this,AllCategories::class.java)
                    startActivity(intent)

                }
                R.id.action_Notifications-> {
                    val intent = Intent(this, NotificationScreen::class.java)
                    startActivity(intent)

                }
                R.id.action_category->{
                    val intent = Intent(this,CategoryScreen::class.java)
                    startActivity(intent)

                }
                R.id.action_products->{


                }

            }

            return@setNavigationItemSelectedListener true


        }


    }
    fun menuFuction() {

           binding.drawerLayout.openDrawer(Gravity.LEFT)
        Timer().schedule(500) {
           lifecycleScope.launch(Dispatchers.Main) {

               setHeaderData()
           }
        }

    }
    override fun onBackPressed() {
        if (  binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            return
        }
        else{
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
    }




    fun homeSelector(){
        binding.menu.setItemSelected(R.id.homeFragment, true)
    }
    fun cartSelector(){
        binding.menu.setItemSelected(R.id.cartFragment, true)
    }
    fun profileSector(){
        binding.menu.setItemSelected(R.id.profileFragment, true)
    }


    fun setHeaderData(){
       val image=findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.headerImage)
      val  headerName = findViewById<TextView>(R.id.headerUsername)
      val  headerEmail = findViewById<TextView>(R.id.headerEmail)

        val img = tinyDB.getString(K.USER_IMG)

        headerName.text = tinyDB.getString(K.USER_NAME)
        headerEmail.text = tinyDB.getString(K.EMAIL)

        Log.d("HeaderTesting"," Value = $img")
        Glide.with(this)
            .load(img)
            .override(300, 200)
            .into(image);
    }
}