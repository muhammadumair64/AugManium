package com.example.augmanium.afterAuth.category

import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil

import com.example.augmanium.R
import com.example.augmanium.afterAuth.category.viewModel.SpecificCategoryProductViewModel
import com.example.augmanium.databinding.ActivitySpecificCategoryProductsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SpecificCategoryProducts : AppCompatActivity() {

    lateinit var binding: ActivitySpecificCategoryProductsBinding
    val viewModel: SpecificCategoryProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_specific_category_products)
        viewModel.allProductRv(binding, this)
       slidingDrawer()
        menuFuction()


    }

    // Sliding Drawer


fun slidingDrawer(){

   binding.naviView.setNavigationItemSelectedListener {
       when (it.itemId) {
           R.id.action_home -> {

           }

           R.id.action_catalog -> {


           }


           R.id.category -> {

               finish()
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


    })


//            val popupMenu: PopupMenu = PopupMenu(this, menubutton)
//            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
//            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
//                when (item.itemId) {
//                    R.id.action_profile ->
//                        Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
//                            .show()
//                    R.id.action_edit ->
//                        Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
//                            .show()
//                    R.id.action_settings ->
//                        Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
//                            .show()
//                }
//                true
//            })
//            popupMenu.show()

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
}
