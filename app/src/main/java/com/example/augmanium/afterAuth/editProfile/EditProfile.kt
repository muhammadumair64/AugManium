package com.example.augmanium.afterAuth.editProfile

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.augmanium.R
import com.example.augmanium.afterAuth.mainActivity.MainActivity
import com.example.augmanium.beforeAuth.SplashScreen
import com.example.augmanium.beforeAuth.editProfileDetectFace.FaceDetection
import com.example.augmanium.dataClass.Mode
import com.example.augmanium.dataClass.User
import com.example.augmanium.dataClass.UserImage
import com.example.augmanium.dataClass.UserInfo
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream


class EditProfile : AppCompatActivity() {

    lateinit var binding: com.example.augmanium.databinding.ActivityEditProfileBinding
    lateinit var tinyDB: TinyDB
    var database: com.google.firebase.database.DatabaseReference = FirebaseDatabase.getInstance().reference
    val userArrayList: ArrayList<UserInfo> = ArrayList()

    var name: String? =""
    var password: String? =""
    var email: String? =""
    var img: String? =""
//    var city: String=""
//    var gender: String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        tinyDB = TinyDB(this)

        binding.backButton.setOnClickListener {
            finish()
        }

    }



    override fun onResume() {
        super.onResume()
        var comesFrom = tinyDB.getInt(K.SIGN_UP)

        initListner()

        if (comesFrom == 2){
            setViews()
        }


        if (comesFrom == 3) {
            img = tinyDB.getString(K.USER_IMG)
            binding.profilePhoto.setImageURI(Uri.parse(img.toString()))
            binding.progressLayout.visibility= View.INVISIBLE

        }
//        }

        binding.logOutBtn.setOnClickListener {
            logOut()
        }
        Log.d("IMAGE", "${img}")

    }

    private fun setViews() {
//        var image
        img = tinyDB.getString(K.USER_IMG)
        val gender = tinyDB.getString(K.GENDER)
        val city = tinyDB.getString(K.CITY)
//        img = image


        getUser()
        Log.d("IMAGE_TT", "${img} $city ")

//        binding.city.setText(city)
//        binding.gender.setText(gender)
        binding.city.setText(city, TextView.BufferType.EDITABLE)
        binding.gender.setText(gender, TextView.BufferType.EDITABLE)


        Glide.with(this)
            .load(img)
            .override(300, 200)
            .into(binding.profilePhoto);
    }


    fun getUser(){

        binding.progressLayout.visibility=View.VISIBLE
        Log.d("User_Email ","${email}")
        val separated: List<String> = email!!.split("@")
        val nodeName = separated[0]
        getUserImage(nodeName)
        database.child("User").child(nodeName).child("User Data").addListenerForSingleValueEvent(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
//                for (snap in snapshot.getChildren()) {
//                    Log.d("REVIEWE_KEY_SNAP ","${snap}")
//                    Log.d("REVIEWE_KEY","${snap.key} ${snap.value}")
                val user = snapshot.getValue(UserInfo::class.java)
                userArrayList.add(user!!)

                binding.progressLayout.visibility= View.INVISIBLE
                Log.d("USER_INFO ","${user.city}   ${user.gender}")
                tinyDB.putString(K.USER_NAME, user!!.userName)
                tinyDB.putString(K.GENDER,user.gender)
                tinyDB.putString(K.CITY,user.city)


//                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")

            }

        })
    }


    fun getUserImage(nodeName: String) {
        Log.d("IMAGE User ","${nodeName}")

//        binding.progressLayout.visibility=View.VISIBLE
        database.child("User").child(nodeName).child("Image").addListenerForSingleValueEvent(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
//                for (snap in snapshot.getChildren()) {
                val image = ""
                Log.d("IMAGE User ","${snapshot.key}   ${snapshot.value}")
                val user = snapshot.getValue(UserImage::class.java)
//                    Log.d("IMAGE User ","${user!!.imgUrl}")
                tinyDB.putString(K.USER_IMG,user!!.imgUrl)
                binding.progressLayout.visibility=View.INVISIBLE
//                    tinyDB.putString(K.USER_NAME, user!!.userName)

//                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR_DATABASE","$error")
                binding.progressLayout.visibility=View.INVISIBLE

            }

        })

    }




    private fun initListner() {

        name = tinyDB.getString(K.USER_NAME)
        email = tinyDB.getString(K.EMAIL)
        password = tinyDB.getString(K.PASSWORD)
        binding.name.text = name
        binding.email.text = email
        binding.password.setText(password, TextView.BufferType.EDITABLE)
        binding.password.isEnabled=false


//        Log.d("Data to upload", "${name} $email $password $city $gender")



        binding.Edit.setOnClickListener {
            val intent = Intent(this, FaceDetection::class.java)
            startActivity(intent)
        }
        binding.saveBtn.setOnClickListener {
            uploadDataToFireBase()

        }
    }

    fun uploadDataToFireBase(){
        val mAuth: FirebaseAuth
        mAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.getCurrentUser()
        val UID = user?.uid
        val separated: List<String> = email!!.split("@")
        val nodeName = separated[0]

        val city = binding.city.text.toString().trim()
        val gender =  binding.gender.text.toString().trim()
        var imageUri = Uri.parse(img)
        uploadToFirebase(imageUri, nodeName)
//        var imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver ,imageUri)
//
//        var imgString = BitMapToString(imageBitmap)

        val userinfo = User(name!!,email!!,UID!!,password!!, city, gender)
        Log.d("Data_FIRE", "${name} $email $password $city $gender")
        val rootRef = FirebaseDatabase.getInstance().reference
        val yourRef = rootRef.child("User").child(nodeName).child("User Data")
        yourRef.setValue(userinfo)

        Toast.makeText(this,"UPLOADED",Toast.LENGTH_SHORT).show()

        if (tinyDB.getInt(K.SIGN_UP) == 2){
            finish()
        }else{
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
            finish()
        }
//        var intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)


    }


    private fun uploadToFirebase(uri: Uri, nodeName: String) {
        val  root: DatabaseReference = FirebaseDatabase.getInstance().getReference("User").child(nodeName);
        val  reference: StorageReference = FirebaseStorage.getInstance().getReference();

        Log.d("IMAGE_URI","Uri... $uri")

        val fileRef: StorageReference = reference.child(nodeName)
        fileRef.putFile(uri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                val model = Mode(uri.toString())
                root.child("Image").setValue(model)
//                progressBar.setVisibility(View.INVISIBLE)
                Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT)
                    .show()
//                imageView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24)
            }
        }.addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
//            fun onProgress(@NonNull snapshot: UploadTask.TaskSnapshot?) {
//                progressBar.setVisibility(View.VISIBLE)
//            }

            override fun onProgress(snapshot: UploadTask.TaskSnapshot) {
                Log.d("Uploaded","DATA_IS $snapshot")
            }
        }).addOnFailureListener {
//            progressBar.setVisibility(View.INVISIBLE)
            Log.d("Uploaded_FAILED"," ${it.localizedMessage}")

            Toast.makeText(this, "Uploading Failed !!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(mUri: Uri): String? {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(mUri))
    }




    fun logOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener { // user is now signed out
//                    startActivity(Intent(this@MyActivity, SignInActivity::class.java))
//                    finish()
                Toast.makeText(this,"Sign out done",Toast.LENGTH_SHORT).show()
                tinyDB.clear()

                var intent = Intent(this,SplashScreen::class.java)
                startActivity(intent)
                finish()
                Log.d("signout ","Done")

            }.addOnFailureListener {
                Log.d("Faild_to_signout"," ${it.localizedMessage}")
            }
    }

    fun BitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

}