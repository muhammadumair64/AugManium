package com.example.augmanium.beforeAuth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.augmanium.R
import com.example.augmanium.afterAuth.mainActivity.MainActivity
import com.example.augmanium.beforeAuth.authViewModel.AuthViewModel
import com.example.augmanium.beforeAuth.signin.SignIn
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    lateinit var continueBtn : RelativeLayout
    val viewModel: AuthViewModel by viewModels<AuthViewModel>()
    lateinit var tinyDB: TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        initViews()
    }

   fun initViews(){
       tinyDB = TinyDB(this)
       continueBtn = findViewById(R.id.continueBtn)
       continueBtn.setOnClickListener {
           permission()
//           per()
       }

    }


    fun contBtn(){
//        continueBtn.setOnClickListener {

            Log.d("CHECKTINY","$tinyDB    ${tinyDB.getString(K.EMAIL)}")
            if (tinyDB.getString(K.EMAIL)!!.isEmpty()){

                val send = Intent(this, SignIn::class.java)
                startActivity(send)


            }else{
                val send = Intent(this, MainActivity::class.java)
                viewModel.getUser(tinyDB.getString(K.EMAIL)!!,this)
                startActivity(send)
                finish()
            }


//        }

    }


    fun permission(){
        val permissions = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val rationale = "Please provide permission so that you can use all functions of app"
        val options: Permissions.Options = Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning")

        Permissions.check(
            this /*context*/,
            permissions,
            rationale,
            options,
            object : PermissionHandler() {
                override fun onGranted() {
                    // do your task.
                    contBtn()
                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String?>?) {
                    // permission denied, block the feature.
                }
            })
    }

    fun per(){

        val permissions =
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        Permissions.check(
            this /*context*/,
            permissions,
            null /*rationale*/,
            null /*options*/,
            object : PermissionHandler() {
                override fun onGranted() {
                    // do your task.
                    contBtn()
                }
            })

    }



}