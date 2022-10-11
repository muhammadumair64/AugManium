package com.example.augmanium

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.augmanium.afterAuth.mainActivity.MainActivity
import com.example.augmanium.beforeAuth.editProfileDetectFace.FaceDetection
import com.example.augmanium.dataClass.Mode
import com.example.augmanium.dataClass.User
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream


class EditProfile : AppCompatActivity() {

    lateinit var binding: com.example.augmanium.databinding.ActivityEditProfileBinding
    lateinit var tinyDB: TinyDB
    lateinit var database: com.google.firebase.database.DatabaseReference
    var name: String? =""
    var password: String? =""
    var email: String? =""
    var img: String? =""
//    var city: String=""
//    var gender: String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile)
    }



    override fun onResume() {
        super.onResume()
        initListner()
        img = tinyDB.getString(K.IMG)
        binding.profilePhoto.setImageURI(Uri.parse(img.toString()))
        binding.logOutBtn.setOnClickListener {
            logOut()
        }
        Log.d("IMAGE", "${img}")

    }

    private fun initListner() {

        database = FirebaseDatabase.getInstance().reference
        tinyDB = TinyDB(this)
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
            Toast.makeText(this,"UPLOAD CLICK",Toast.LENGTH_SHORT).show()
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
        val yourRef = rootRef.child("User").child(nodeName)
        yourRef.setValue(userinfo)

//        var intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)


    }


    private fun uploadToFirebase(uri: Uri, nodeName: String) {
        val  root: DatabaseReference = FirebaseDatabase.getInstance().getReference("User").child(nodeName);
        val  reference: StorageReference = FirebaseStorage.getInstance().getReference();

        val fileRef: StorageReference = reference.child(nodeName)
        fileRef.putFile(uri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                Log.d("IMAGE_URI","Uri... $uri")
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