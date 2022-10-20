package com.example.augmanium.beforeAuth.editProfileDetectFace.viewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.provider.MediaStore

import android.graphics.Bitmap

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

import java.io.ByteArrayOutputStream
import android.graphics.RectF
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.face.FaceLandmark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.io.File
import android.hardware.camera2.CameraCharacteristics

import android.hardware.camera2.CameraManager
import com.example.augmanium.databinding.ActivityFaceDetectionBinding
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB


@HiltViewModel
class FaceVerificationViewModel @Inject constructor() : ViewModel() {
    var currentPhotoPath = ""
    var faceListSize = 0;
    lateinit var activityContext: Context
    lateinit var tinyDB: TinyDB
    lateinit var activityBinding: ActivityFaceDetectionBinding
    var imageUri: Uri? = null
    var v: View? = null

    fun activityListners(binding: ActivityFaceDetectionBinding, context: Context) {
        activityContext = context
        activityBinding = binding
        tinyDB = TinyDB(context)
        binding.ok.setOnClickListener {

            if (faceListSize == 1) {
//                binding.progressLayout.visibility=View.VISIBLE
                val returnIntent = Intent()
                returnIntent.putExtra("ImageUri", imageUri.toString())
                Log.d("IMAGE","IMAGE URI FVM $imageUri")
                val m = tinyDB.putString(K.USER_IMG, imageUri.toString())
                tinyDB.putInt(K.SIGN_UP,3)
                Log.d("IMAGE","IMAGE URI tiny $m")

                (context as Activity).setResult(1122, returnIntent)
                (context as Activity).finish()
            } else {
                Toast.makeText(
                    activityContext,
                    "Please capture pic with your face",
                    Toast.LENGTH_SHORT
                ).show();
            }

        }
//        binding.cancel.setOnClickListener {
//            (context as Activity).finish()
////            var intent = Intent(context, ExamSetupScreen::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
////            startActivity(context, intent, null)
//
//        }
    }

    fun initImageUri(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
//            Log.d("TheLandmarks", "In uri $uri")
            detectingFace(bitmap, activityContext)
        }

    }

    suspend fun detectingFace(myBitmap: Bitmap, context: Context) {

        // currImageURI is the global variable I'm using to hold the content:// URI of the image
//        var `is`: InputStream? = null
//        try {
//            `is` = contentResolver.openInputStream(uri)
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//        val myBitmap = BitmapFactory.decodeStream(`is`)

        val myRectPaint = Paint()
        myRectPaint.strokeWidth = 15f
        myRectPaint.color = Color.BLUE
        myRectPaint.style = Paint.Style.STROKE
        Log.d("TheLandmarks", "My Bitmap $myBitmap")
        val tempBitmap =
            Bitmap.createBitmap(myBitmap.width, myBitmap.height, Bitmap.Config.RGB_565)
        val tempCanvas = Canvas(tempBitmap)
        tempCanvas.drawBitmap(myBitmap, 0f, 0f, null)

        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        val faceDetector = FaceDetection.getClient(highAccuracyOpts)
        val image = InputImage.fromBitmap(myBitmap, 0)


        faceDetector.process(image)
            .addOnSuccessListener { faces ->
                faceListSize = faces.size
                when (faces.size) {
                    1 -> {
                        val face = faces[0]
                        Log.d("TheLandmarks ", "Size of landaMarks${face.allLandmarks?.size}")

//                        println("The Landmarks ${face.allLandmarks.size}")
                        var isMouthCorrect = true
                        var isRightEyeCorret = true

                        var isRightCheekCorrect = true
                        var isLeftCheekCorrect = true
                        Log.d("TheLandmarks ", "${face.allLandmarks?.size}")
                        for (mark in face.allLandmarks) {
                            Log.d("TheLandmarks", "inside for loop.")
                            when (mark.landmarkType) {

                                FaceLandmark.MOUTH_BOTTOM -> {
                                    isMouthCorrect = mark.position.y < myBitmap.height
                                    Log.d(
                                        "TheLandmarks",
                                        "Got Mouth  $isMouthCorrect  ${mark.position.y}   ${myBitmap.height}"
                                    )
                                }


                                FaceLandmark.RIGHT_EYE -> {
                                    isRightEyeCorret =
                                        mark.position.y > activityBinding.userImage.top
                                    Log.d(
                                        "TheLandmarks",
                                        "Got Rigth Eye ${mark.position.x}   ${activityBinding.userImage.top}"
                                    )
                                }

                                FaceLandmark.RIGHT_CHEEK -> {
                                    isRightCheekCorrect = mark.position.x < myBitmap.width
                                    Log.d("TheLandmarks", "FaceLandmark.RIGHT_CHEEK")
                                }

                                FaceLandmark.LEFT_CHEEK -> {
                                    isLeftCheekCorrect =
                                        mark.position.x > activityBinding.userImage.left

                                    Log.d("TheLandmarks", "FaceLandmark.RIGHT_CHEEK")
                                }


                            }

                            Log.d(
                                "TheLandmarks ",
                                "${mark.position}   and mark is ${mark.landmarkType}"
                            )

                        }

                        if (isRightEyeCorret && isMouthCorrect && isRightCheekCorrect && isLeftCheekCorrect) {

                            Log.d(
                                "TheLandmarks ",
                                "isRightEyeCorret in true block...$isRightEyeCorret isMouthCorrect...$isMouthCorrect isRightCheekCorrect...$isRightCheekCorrect isLeftCheekCorrect...$isLeftCheekCorrect"
                            )
                            val rect = RectF(faces[0].boundingBox)
                            tempCanvas.drawRoundRect(rect, 2f, 2f, myRectPaint)
                            imageUri = getImageUri(activityContext, tempBitmap)!!
                        } else {
                            Log.d(
                                "TheLandmarks ",
                                "isRightEyeCorret...$isRightEyeCorret isMouthCorrect...$isMouthCorrect isRightCheekCorrect...$isRightCheekCorrect isLeftCheekCorrect...$isLeftCheekCorrect"
                            )
                            activityBinding.faceRecTv.text = "Incomplete Face Detected"
                            faceListSize = -1
                        }


                        hideOrShowRetryImage()
                    }
                    0 -> {
                        activityBinding.faceRecTv.text = "No Face Detected"
                        hideOrShowRetryImage()
                        Toast.makeText(activityContext, "No Face Detected", Toast.LENGTH_SHORT)
                            .show();
                    }
                    else -> {

                        for (face in faces) {

                            val rect = RectF(face.boundingBox)
                            tempCanvas.drawRoundRect(rect, 2f, 2f, myRectPaint)
                        }
                        imageUri = getImageUri(activityContext, tempBitmap)!!
                        activityBinding.faceRecTv.text = "Multiple Face Detected"
                        hideOrShowRetryImage()
                        Toast.makeText(
                            activityContext,
                            "Multiple Face Detected",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }

                if (imageUri != null) {
//                    activityBinding.progressLayout.visibility=View.GONE
                    activityBinding.userImage.setImageURI(imageUri)
                } else {
//                    activityBinding.progressLayout.visibility=View.GONE
                    activityBinding.userImage.setImageBitmap(myBitmap)
                }

            }
            .addOnFailureListener { e ->
//                activityBinding.progressLayout.visibility=View.GONE
                Toast.makeText(activityContext, "Unable to run detection", Toast.LENGTH_SHORT)
                    .show();
            }

    }

    fun hideOrShowRetryImage() {
        Log.d("The Land Mark ", "In Retry Funbction")
        if (faceListSize != 1) {
            activityBinding.apply {
                faceRecTv.visibility = View.VISIBLE
                retryBtn.visibility = View.VISIBLE

                retryBtn.setOnClickListener {
                    this@FaceVerificationViewModel.cameraIntent()
                }
            }
        } else {
            activityBinding.apply {
                faceRecTv.visibility = View.INVISIBLE
                retryBtn.visibility = View.INVISIBLE
            }
        }
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                System.currentTimeMillis().toString(),
                null
            )
        return Uri.parse(path)
    }


    fun cameraIntent() {

        val fileName = "MyProfilePicForAi"
        var file: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        var imageFile = File.createTempFile(fileName, ".jpg", file)
        currentPhotoPath = imageFile.absolutePath
        val uri = FileProvider.getUriForFile(
            activityContext,
            "com.example.augmanium.fileprovider",
            imageFile
        )
        /**
         * Camera changes
         */
        val manager = (activityContext as Activity).getSystemService(Context.CAMERA_SERVICE)
        var cameraid = getFrontFacingCameraId(manager as CameraManager)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
        cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
        Log.d("CameraTesting","CameraId$cameraid")
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        Log.d("IMAGE","IMAGE URI FVM22 $uri")
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", cameraid?.toInt())
        (activityContext as com.example.augmanium.beforeAuth.editProfileDetectFace.FaceDetection).startActivityForResult(cameraIntent, 123)
    }



    fun getFrontFacingCameraId(cManager: CameraManager): String? {
        for (cameraId in cManager.cameraIdList) {
            val characteristics = cManager.getCameraCharacteristics(cameraId!!)
            val cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING)!!
            if (cOrientation == CameraCharacteristics.LENS_FACING_FRONT){
                return cameraId
                break
            }
        }
        return null
    }





    fun checkImageOrientation(bitmap: Bitmap):Bitmap {

        var  angle=0
        val ei = ExifInterface(currentPhotoPath)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        var rotatedBitmap: Bitmap? = null
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                angle=90
                 rotatedBitmap =
                    rotateImage(bitmap, angle.toFloat())
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                angle=180
                rotatedBitmap =
                    rotateImage(bitmap, angle.toFloat())
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                 angle=270
                rotatedBitmap =
                    rotateImage(bitmap,angle.toFloat() )
            }
            ExifInterface.ORIENTATION_NORMAL -> {
                rotatedBitmap = bitmap
            }
            else -> {
                rotatedBitmap = bitmap
            }
        }
        return rotatedBitmap!!
    }


    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }
}
