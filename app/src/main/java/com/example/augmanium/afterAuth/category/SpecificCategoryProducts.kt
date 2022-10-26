package com.example.augmanium.afterAuth.category

import android.content.Intent
import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide

import com.example.augmanium.R
import com.example.augmanium.afterAuth.category.viewModel.SpecificCategoryProductViewModel
import com.example.augmanium.afterAuth.mainActivity.MainActivity
import com.example.augmanium.afterAuth.notification.NotificationScreen
import com.example.augmanium.databinding.ActivitySpecificCategoryProductsBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule


@AndroidEntryPoint
class SpecificCategoryProducts : AppCompatActivity() {

    lateinit var binding: ActivitySpecificCategoryProductsBinding
    val viewModel: SpecificCategoryProductViewModel by viewModels()
    lateinit var image :de.hdodenhof.circleimageview.CircleImageView
    lateinit var headerEmail : TextView
    lateinit var headerName: TextView
    lateinit var tinyDB:TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_specific_category_products)
        viewModel.allProductRv(binding, this)
        tinyDB= TinyDB(this)
       slidingDrawer()
        menuFuction()


    }

    // Sliding Drawer


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
   binding.menu.setOnClickListener(View.OnClickListener {

// get User Information
        try {


        } catch (e: Exception) {
            println(e.localizedMessage)

        }
        // open drawer here
    binding.drawerLayout.openDrawer(Gravity.LEFT)

       Timer().schedule(500) {
           lifecycleScope.launch(Dispatchers.Main) {

               setHeaderData()
           }
       }

    })



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


    fun setHeaderData(){
        image=findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.headerImage)
        headerName = findViewById<TextView>(R.id.headerUsername)
        headerEmail = findViewById<TextView>(R.id.headerEmail)
        headerName.text = tinyDB.getString(K.USER_NAME)
        headerEmail.text = tinyDB.getString(K.EMAIL)

        val img = tinyDB.getString(K.USER_IMG)
        Log.d("HeaderTesting"," Value = $img")
        Glide.with(this)
            .load(img)
            .override(300, 200)
            .into(image);
    }
}
