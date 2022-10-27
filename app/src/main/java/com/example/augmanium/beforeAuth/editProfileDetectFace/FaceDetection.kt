package com.example.augmanium.beforeAuth.editProfileDetectFace

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.augmanium.R
import com.example.augmanium.beforeAuth.editProfileDetectFace.viewModel.FaceVerificationViewModel
import com.example.augmanium.databinding.ActivityFaceDetectionBinding
import java.io.File

class FaceDetection : AppCompatActivity() {

    val viewModel: FaceVerificationViewModel by viewModels()
    val context: Context = this

    lateinit var binding: ActivityFaceDetectionBinding
    lateinit var file: File
    val TAG = "FACE_VERIFICATION_SCREEN"
    var mCurrentPhotoPath: String? = null
    var REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_face_detection)

        val linrtl = binding.layout as RelativeLayout
        linrtl.layoutDirection = View.LAYOUT_DIRECTION_LTR
//        updateLanguage()
        viewModel.activityListners(binding, this)

        viewModel.cameraIntent()
//        intentCameraImage()
//        getImage()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("REQUEST_CODE", "REQUEST_CODE_ $requestCode .... ${data?.extras}")

        try {
            if (requestCode == 123) {
                binding.progressLayout.visibility=View.VISIBLE
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                val bitmap = BitmapFactory.decodeFile(viewModel.currentPhotoPath)
                Log.d("INTENT_DELAY","bitmap=$bitmap")

                val rotatedBitmap = viewModel.checkImageOrientation(bitmap)
//            val cameranew = rotateImage(bitmap, 270)
//            Log.d("CameraTesting", "Bitmap ${bitmap}")
                viewModel.initImageUri(rotatedBitmap)
            }
        } catch (e: Exception) {
            e.localizedMessage
            binding.progressLayout.visibility=View.INVISIBLE
            finish()
        }


    }


//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.d("REQUEST_CODE", "REQUEST_CODE_ $requestCode .... ${data?.extras}")
//
//        try {
//            if (requestCode == 123) {
////                binding.progressLayout.visibility=View.VISIBLE
//                val options = BitmapFactory.Options()
//                options.inJustDecodeBounds = true
//                val bitmap = BitmapFactory.decodeFile(viewModel.currentPhotoPath)
//                Log.d("INTENT_DELAY","bitmap=$bitmap")
//
//                val rotatedBitmap = viewModel.checkImageOrientation(bitmap)
////            val cameranew = rotateImage(bitmap, 270)
////            Log.d("CameraTesting", "Bitmap ${bitmap}")
//                viewModel.initImageUri(rotatedBitmap)
//            }
//        } catch (e: Exception) {
//            e.localizedMessage
//        }
//
//
//    }
}