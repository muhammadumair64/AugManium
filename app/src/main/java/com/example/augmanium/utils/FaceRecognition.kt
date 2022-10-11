package com.example.augmanium.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.Log
import com.example.augmanium.CreateNewPassword
import com.example.augmanium.R
import com.example.augmanium.beforeAuth.ScanFace
import com.google.firebase.auth.FirebaseAuth
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


class FaceRecognition {

//    var baseViewModel = BaseViewModel()
    lateinit var context: Context
    lateinit var tinyDB: TinyDB

    lateinit var detector: com.google.mlkit.vision.face.FaceDetector
    lateinit var tfLite: Interpreter
    var flipX = true
    var inputSize = 112
    lateinit var intValues: IntArray
    var isModelQuantized = false
    var IMAGE_MEAN = 128.0f
    var IMAGE_STD = 128.0f
    var embeedings: Array<Array<FloatArray>> =
        arrayOf(Array(1) { FloatArray(OUTPUT_SIZE) }, Array(1) { FloatArray(OUTPUT_SIZE) })
    lateinit var bitmapArray: ArrayList<Bitmap>
    var OUTPUT_SIZE = 192 //Output size of model
    var isFaceMatched = false
    var TAG = "FACE_RECOGNITION"
    var forgotPass = true


    init {
        embeedings[0] = Array(1) { FloatArray(3) }
        embeedings[1] = Array(1) { FloatArray(3) }

    }


    suspend fun detectoreAnalyze(
        idImageBitmap: Bitmap,
        activityContext: Context,
        index: Int,
        action: () -> Unit,
        action2: () -> Unit
    ) {

        Log.d(TAG, "DETECTOR_ANALYZE")
        Log.d(TAG, "student Image $idImageBitmap")

        this.context = activityContext
        tinyDB = TinyDB(context)

        bitmapArray = ArrayList()

        //Load model
        try {
//            tfLite = loadModelFile(this, modelFile)?.let { Interpreter(it) }!!
            tfLite = Interpreter(loadModelFile(context as Activity)!!)
        } catch (e: IOException) {
            Log.d(TAG, "Load Model Exception ${e.localizedMessage}")
            e.printStackTrace()
        }
        //Initialize Face Detector
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .build()
        detector = FaceDetection.getClient(highAccuracyOpts)


        val inputImage = InputImage.fromBitmap(idImageBitmap, 0)

        val rotationDigree = inputImage.rotationDegrees


        Log.d(TAG, "Input image $inputImage")
        //idImageBitmap.compress(Bitmap.CompressFormat.JPEG, 30, fileOutputStream)

        CoroutineScope(Job()).launch(Dispatchers.IO) {

            detector.process(inputImage)
                .addOnSuccessListener {
                    if (it.size > 1) {

                        if (index == 0) {
//                            ExamSetupScreen.faceDetectIdImage = false
//                            LiveStreamVerificationMoodle.imageExist = false
                            action()
                        }

//                        Toast.makeText(
//                            context,
//                            "More then one face not allowed",
//                            Toast.LENGTH_SHORT
//                        ).show()

//                        ExamSetupScreen.faceDetectIdImage = false
//                        LiveStreamVerificationMoodle.imageExist = false
                        action()

//                        Toast.makeText(
//                            context,
//                            "More then one face not allowed",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        Log.d("more faces", "more then 1 face")
                    }
                    else if (it.size == 0) {
                        Log.d(TAG,"it.size==0")
                        if (index == 0) {
                            Log.d(TAG,"index==0")
//                            ExamSetupScreen.faceDetectIdImage = false
//                            LiveStreamVerificationMoodle.imageExist = false
                            action()
                        }
//                      Toast.makeText(context, "No face found", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val face = it.get(0)
                        if (index == 0) {
                            Log.d(TAG,"Face is detected")
//                            ExamSetupScreen.faceDetectIdImage = true
//                            LiveStreamVerificationMoodle.imageExist = true

                        }
                        //Adjust orientation of Face
                        val frame_bmp1: Bitmap = rotateBitmap(
                            idImageBitmap!!,
                            rotationDigree,
                            false,
                            false
                        )

                        //Get bounding box of face
                        val boundingBox = RectF(face.boundingBox)

                        when (it.size) {
                            1 -> {
                                Log.d("size", "size....${it.size}")
                                //Crop out bounding box from whole Bitmap(image)
                                var cropped_face: Bitmap =
                                    getCropBitmapByCPU(frame_bmp1, boundingBox)

                                if (flipX) {
                                    cropped_face = rotateBitmap(
                                        cropped_face,
                                        0,
                                        flipX,
                                        false
                                    )
                                    //Scale the acquired Face to 112*112 which is required input for model
                                    val scaled: Bitmap = getResizedBitmap(cropped_face, 112, 112)


                                    bitmapArray.add(scaled)





                                    Log.d(TAG, "size of bitmapArray...$index${bitmapArray.size}")


                                        recognizeImage(scaled, index) {   action2() }





                                }

                                Log.d("List of faces ", "Faces detcted ${it.size}")
                            }
                            else -> {
                                Log.d("List of faces ", "Faces detcted ${it.size}")
                            }
                        }

                    }

                }
                .addOnFailureListener {
                    Log.d("FACE_REC", "Failed in detecor ${it.localizedMessage}")
                }
        }


    }


    fun recognizeImage(bitmap: Bitmap, index: Int, action: () -> Unit) {

        Log.d(TAG, "RECOGNITION_START")

        // set Face to Preview ##croppes##
        // face_preview.setImageBitmap(bitmap)

//        binding.cameraImage.setImageBitmap(bitmap)

        //Create ByteBuffer to store normalized image
        val imgData = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * 4)
        imgData.order(ByteOrder.nativeOrder())
        intValues = IntArray(inputSize * inputSize)

        //get pixel values from Bitmap to normalize
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        imgData.rewind()
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixelValue: Int = intValues.get(i * inputSize + j)
                if (isModelQuantized) {
                    // Quantized model
                    imgData.put((pixelValue shr 16 and 0xFF).toByte())
                    imgData.put((pixelValue shr 8 and 0xFF).toByte())
                    imgData.put((pixelValue and 0xFF).toByte())
                } else { // Float model
                    imgData.putFloat(((pixelValue shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData.putFloat(((pixelValue shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData.putFloat(((pixelValue and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                }
            }
        }
        //imgData is input to our model
        val inputArray = arrayOf<Any>(imgData)
        val outputMap: MutableMap<Int, Any> = HashMap()

        Log.d("FACE_REC", "Embedding size is ${embeedings.size} Index is ${index}")
        if (index < embeedings.size) {
//            embeedings.add(index,Array(1) { FloatArray(OUTPUT_SIZE) })
            embeedings[index] = Array(1) { FloatArray(OUTPUT_SIZE) }
        }

        //output of model will be stored in this variable


//        if (index < bitmapArray.size){
//            bitmapArray[index] = scaled
//        }else{
//            bitmapArray.add(scaled)
//        }
//
//        if(embeedings.size > index)  {
//            outputMap[0] = embeedings.get(0)
//        } else{
//            outputMap[0] = embeedings.get(1)
//        }

        outputMap[0] = embeedings[index]
        tfLite.runForMultipleInputsOutputs(inputArray, outputMap) //Run model
        var distance = Float.MAX_VALUE
        val id = "0"
        var label: String? = "?"

        Log.d(TAG, "1 embeeding")

        // End Of Image Processing

        action()


    }


    fun compareFaces(): Boolean {

        //Compare new face with saved Faces.
        CoroutineScope(Job()).launch(Dispatchers.IO) {

            if (embeedings.size == 2) {
                val isMatched: Float? =
                    findNearest(embeedings.get(0).get(0)) //Find closest matching face

                Log.d(
                    TAG,
                    "embeeding get.... ${embeedings.get(0).get(0)}.....${embeedings.get(1)[0]}"
                )

                if (isMatched != null) {
                    Log.d(TAG, "ismatck k andr")
                    Log.d("match", "----------- is $isMatched")

                    /**
                     * umair's testing
                     * MY CHANGES I AM GOING TO REPLACE VALUE (1.000F) BY (0.9) ON MY ASSUMPTION
                     */
                    if (isMatched < 0.9f) //If distance between Closest found face is more than 1.000 ,then output UNKNOWN face.
                    {

                        isFaceMatched = true
                        val email = tinyDB.getString(K.EMAIL)
                        Log.d("FACE_REC_RESULT", "Same face $email")
//                        BaseClass.runOnMain {
//                            Toast.makeText(context, "Same Face", Toast.LENGTH_SHORT).show()
//                        }


                        if(forgotPass){
                            forgotPass = false
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email!!)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "Email sent.")

                                        val intent = Intent(context,CreateNewPassword::class.java)
                                        (context as ScanFace).startActivity(intent)
                                        (context as ScanFace).finish()
                                    }
                                    else if (task.isCanceled){
                                        Log.d(TAG, "Email sent. Canceled")
                                        (context as ScanFace).finish()
                                    }else{
                                        Log.d(TAG, "Email sent. ${task.exception!!.localizedMessage}")
                                        (context as ScanFace).finish()
                                    }
                                }
                        }



                    } else {
                        isFaceMatched = false

//                        BaseClass.runOnMain {
//                            Toast.makeText(context, "Face not match", Toast.LENGTH_SHORT).show()
//                        }

                        Log.d("FACE_REC_RESULT", "Face not match")
                    }
//                embeedings= emptyArray()
//                Log.d("Embeddings", "Empty....${embeedings[0]}...${embeedings[1]}")

                }


            }
        }


        return isFaceMatched
    }

    private fun findNearest(emb: FloatArray): Float? {
        var ret: Float? = null

        val knownEmb = embeedings[1][0]
        var distance = 0f
        Log.d("Embeddings Size", "Sizee...${emb.indices}")
        //0..191
        for (i in emb.indices) {
            val diff = emb[i] - knownEmb[i]
            Log.d("Diff", "value$diff")
            distance += diff * diff
            Log.d("Distance", "value ....$distance")
        }
        distance = Math.sqrt(distance.toDouble()).toFloat()
        if (ret == null || distance < ret) {
            ret = distance
        }

        return ret
    }


    private fun getCropBitmapByCPU(source: Bitmap?, cropRectF: RectF): Bitmap {
        val resultBitmap = Bitmap.createBitmap(
            cropRectF.width().toInt(),
            cropRectF.height().toInt(), Bitmap.Config.ARGB_8888
        )

        val cavas = Canvas(resultBitmap)

        // draw background
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        paint.color = Color.WHITE
        cavas.drawRect( //from  w w  w. ja v  a  2s. c  om
            RectF(0F, 0F, cropRectF.width(), cropRectF.height()),
            paint
        )
        val matrix = Matrix()
        matrix.postTranslate(-cropRectF.left, -cropRectF.top)
        cavas.drawBitmap(source!!, matrix, paint)
        if (source != null && !source.isRecycled) {
            source.recycle()
        }
        return resultBitmap
    }

    @Throws(IOException::class)
    private fun loadModelFile(activity: Activity): MappedByteBuffer? {


        val fileDescriptor = activity.resources.openRawResourceFd(R.raw.mobile_face_net)
        Log.d("AssetManager", "Asset Open")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun rotateBitmap(
        bitmap: Bitmap, rotationDegrees: Int, flipX: Boolean, flipY: Boolean
    ): Bitmap {
        val matrix = Matrix()

        // Rotate the image back to straight.
        matrix.postRotate(rotationDegrees.toFloat())

        // Mirror the image along the X or Y axis.
        matrix.postScale(if (flipX) -1.0f else 1.0f, if (flipY) -1.0f else 1.0f)
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        // Recycle the old bitmap if it has changed.
        if (rotatedBitmap != bitmap) {
            bitmap.recycle()
        }
        return rotatedBitmap
    }

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }



}