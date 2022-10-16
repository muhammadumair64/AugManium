package com.example.augmanium.beforeAuth.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.hardware.camera2.CaptureRequest
import android.media.ExifInterface
import android.media.Image
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.util.Range
import android.util.Size
import android.widget.Toast
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.augmanium.beforeAuth.ScanFace
import com.example.augmanium.databinding.ActivityScanFaceBinding
import com.example.augmanium.utils.FaceRecognition
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.nio.ReadOnlyBufferException
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.experimental.inv


@HiltViewModel
class ViewModelScanFace @Inject constructor(
//    val tinyDB: TinyDB,
//    val examRepository: ExamRepository

) : ViewModel() {

    var getFile: File? = null
    var TAG = "ViewModelLiveVerification"
    var cameraSelector: CameraSelector? = null
    lateinit var activityContext: Context
    lateinit var binding: ActivityScanFaceBinding
    lateinit var preview : Preview
    lateinit var tinyDB: TinyDB
    var mediaImage: Image? = null
    var faceRecognition = FaceRecognition()
    var counter = 0
    var imageBitmap: Bitmap? = null
//    lateinit var examSetupAPIS: ExamSetupAPIS
    private var mWidth = 0
    private var mHeight = 0
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    var cameraProvider: ProcessCameraProvider? = null
//    val loggedInMethod = tinyDB.getInt(K.CHOOSE_LOGIN_METHOD)
    var imagePath = ""


    fun initViews(
        binding: ActivityScanFaceBinding,
        activityContext: Context
    ) {
        this.activityContext = activityContext
        this.binding = binding
        tinyDB = TinyDB(activityContext)
        Log.d(TAG, "initViews")

        convertImageUrlToBitmap()
    }


//    fun checkCameraPermission(activityContext: Context, action: () -> Unit) {
//        this.activityContext = activityContext
////        examSetupAPIS = ExamSetupAPIS(tinyDB, activityContext, examRepository)
////        examSetupAPIS.initPermission(activityContext, arrayOf(
//            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        ), { action() }, {
//            moveToCheckPermisson(false)
//        })
//    }


    fun convertImageUrlToBitmap() {
        Log.d(TAG, "convertImageUrlToBitmap")
        try {
//            val url = URL("https://pbs.twimg.com/media/EbSdWtTXsAEa05Y.jpg")

//            binding.progressLayout.visibility = View.VISIBLE





            viewModelScope.launch(Dispatchers.IO) {


                Log.d(TAG, "Image Pixels$imageBitmap")


//                val url = File(
//                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
//                    "/Tiny/mypic.jpg"
//                )
//                Log.d("FILE_PATH",url.toString())
//                imageBitmap = BitmapFactory.decodeFile(url.absolutePath)
////                binding.img.setImageBitmap(imageBitmap)
//                Log.d("FILE_BITMAP",imageBitmap.toString())
                var user_img = tinyDB.getString(K.USER_IMG)

                try {
                    val url = URL(user_img)
                    imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                } catch (e: IOException) {
                    println(e)
                }
//                imageBitmap = StringToBitMap(user_img)
//                imageBitmap = MediaStore.Images.Media.getBitmap(activityContext.contentResolver ,imageUri)



                withContext(Dispatchers.Main)
                {



                    faceRecognition.detectoreAnalyze(imageBitmap!!, activityContext, 1, {

                        Toast.makeText(
                            activityContext,
                            "No Face in Profile image.",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    },
                        {
//                        binding.progressLayout.visibility = View.GONE
                            cameraBind()
                            startRunner()
                        })


                    cameraBind()
                    startRunner()
                }


            }


        } catch (e: IOException) {
            System.out.println(e)
        }
    }


    private fun cameraBind() {
        Log.d("CameraBind", "Creating an instance")
        cameraProviderFuture = ProcessCameraProvider.getInstance(activityContext)
        cameraProviderFuture!!.addListener({
            try {
                cameraProvider = cameraProviderFuture!!.get()
                bindPreview(cameraProvider!!)
            } catch (e: ExecutionException) {
                // No errors need to be handled for this in Future.
                // This should never be reached.
            } catch (e: InterruptedException) {
            }
        }, ContextCompat.getMainExecutor(activityContext))
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
Log.d("PREVIEW"," Binding preview")
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        viewModelScope.launch(Dispatchers.Main){
            preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                    Log.d("PREVIEW"," preview done")
                }
        }
//        withContext(Dispatchers.Main){}


        val builder = ImageAnalysis.Builder()
        val ext: Camera2Interop.Extender<*> = Camera2Interop.Extender(builder)

        ext.setCaptureRequestOption(
            CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE,
            Range<Int>(1, 5)
        )
        val executor: Executor = Executors.newSingleThreadExecutor()
        val imageAnalysis = builder
            .setTargetResolution(Size(300, 350))
            .setBackgroundExecutor(executor)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) //Latest frame is shown
            .build()

        imageAnalysis.setAnalyzer(executor, { imageProxy ->
// Camera Feed-->Analyzer-->ImageProxy-->mediaImage-->InputImage(needed for ML kit face detection)

            val captureImage = imageProxy.image
            mediaImage = captureImage
            Log.d(TAG, "Media image $mediaImage")

            println("Rotation_Degrees " + imageProxy.imageInfo.rotationDegrees)

            val file = getOutputMediaFile("RecognizeImage")
//
            val imageToBitmap = toBitmap(mediaImage!!)


            val bos = ByteArrayOutputStream()
            imageToBitmap?.compress(
                Bitmap.CompressFormat.JPEG,
                100 /*ignored for PNG*/,
                bos
            )
            val bitmapdata: ByteArray = bos.toByteArray()

            val fileStream = FileOutputStream(file)
            fileStream.write(bitmapdata)
            fileStream.flush()
            fileStream.close()



            faceRecognition(imageProxy.imageInfo.rotationDegrees)

            Log.d(TAG, "Image $captureImage")


            //Process acquired image to detect faces
            imageProxy.close()
        })

        try {
            val owner = activityContext as LifecycleOwner
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(owner, cameraSelector!!, preview, imageAnalysis)
        } catch (e: Exception) {
            Log.d(TAG, "Exception on bindPreview ${e.localizedMessage}")
        }

    }

    private fun startRunner() {
        Log.d(TAG, "Inside start Runner function")

        (activityContext as ScanFace).timerExecutor.scheduleAtFixedRate(object :
            TimerTask() {
            override fun run() {
                Log.d(TAG, "Inside timer run function.")
                if (counter < 30) {
                    Log.d(TAG, "seconds")

                    counter++
                    viewModelScope.launch {
                        if (counter > 1) {
//                            binding.timer.text = "$counter Seconds"

                        } else {
//                            binding.timer.text = "$counter Second"
                        }
                    }


                } else {
                    Log.d(TAG, "Counter $counter")
                    counter = 0
                    (activityContext as ScanFace).timerExecutor.cancel()
                    (activityContext as ScanFace).timerExecutor.purge()
//                    moveToCheckPermisson(false)
                }
            }
        }, 0, 1000)


    }

    fun faceRecognition(rotationDegrees: Int) {
        if (mediaImage != null) {

            getFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "/RecognizeImage/CaptureImage.jpg"
            )
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val imageBitmap = BitmapFactory.decodeFile(getFile!!.absolutePath)




            viewModelScope.launch(Dispatchers.IO) {
                Log.d(TAG, "imageToBitmap$imageBitmap")

                if (imageBitmap != null) {
                    val rotatedBitmap = rotateImage(imageBitmap, rotationDegrees.toFloat())


                    Log.d(TAG, "RotatedBitmap..$rotatedBitmap")
//                        withContext(Dispatchers.Main) {
//                            binding.profileImage.setImageBitmap(rotatedBitmap)
//                        }
                    faceRecognition.detectoreAnalyze(
                        rotatedBitmap!!,
                        activityContext,
                        0, {
                            Log.d(TAG, "Face not detected.")
                            bindPreview(cameraProvider!!)
                        }, {
                            Log.d(TAG, "Before compare the face.")

                            if (faceRecognition.compareFaces()) {
                                Log.d(TAG, "Faces are compare")
//                                (activityContext as LiveStreamVerificationMoodle).timerExecutor.cancel()
//                                (activityContext as LiveStreamVerificationMoodle).timerExecutor.purge()
//                                moveToCheckPermisson(true)

                            } else {
                                Log.d(TAG, "Face not match")
                                bindPreview(cameraProvider!!)
//                                    startRunner()
                            }
                        })
                    bindPreview(cameraProvider!!)



                } else {
                    Log.d(TAG, "Bitmap Image is null")
                }

            }
        }
    }

//    fun moveToCheckPermisson(checkCompareFace: Boolean) {
//        if (checkCompareFace) {
//            val intent = Intent(activityContext, CheckPermissionsScreen::class.java)
//            intent.flags =
//                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            ContextCompat.startActivity(activityContext, intent, null)
//        } else {
//            val intent = Intent(activityContext, CheckPermissionsScreen::class.java)
//            intent.putExtra(K.FACE_NOT_VERIFY_MOODLE, true)
//            intent.flags =
//                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            ContextCompat.startActivity(activityContext, intent, null)
//        }
//
//    }

    private fun getOutputMediaFile(folderName: String): File? {

//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val folder_gui =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + File.separator + folderName)
        if (!folder_gui.exists()) {
            folder_gui.mkdirs()
        }
        return File(folder_gui.absolutePath, "CaptureImage.jpg")
    }


    private fun toBitmap(image: Image): Bitmap? {
        Log.d(TAG, "TO_BITMAP_IMAGE $image")
        val arr = YUV_420_888toNV21(image)
        if (arr != null) {
            val nv21: ByteArray = arr
            val yuvImage = YuvImage(nv21, ImageFormat.NV21, image!!.width, image.height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 75, out)
            val imageBytes = out.toByteArray()
            //System.out.println("bytes"+ Arrays.toString(imageBytes));

            //System.out.println("FORMAT"+image.getFormat());
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }

        return null
    }


    fun YUV_420_888toNV21(image: Image): ByteArray? {
        Log.d(TAG, "YUV_420_IMAGE $image")
        val width = image.width
        val height = image.height
        val ySize = width * height
        val uvSize = width * height / 4
        val nv21 = ByteArray(ySize + uvSize * 2)
        val yBuffer = image.planes[0].buffer // Y
        val uBuffer = image.planes[1].buffer // U
        val vBuffer = image.planes[2].buffer // V
        var rowStride = image.planes[0].rowStride
        assert(image.planes[0].pixelStride == 1)
        var pos = 0
        if (rowStride == width) { // likely
            yBuffer[nv21, 0, ySize]
            pos += ySize
        } else {
            var yBufferPos = -rowStride.toLong() // not an actual position
            while (pos < ySize) {
                yBufferPos += rowStride.toLong()
                yBuffer.position(yBufferPos.toInt())
                yBuffer[nv21, pos, width]
                pos += width
            }
        }
        rowStride = image.planes[2].rowStride
        val pixelStride = image.planes[2].pixelStride
        assert(rowStride == image.planes[1].rowStride)
        assert(pixelStride == image.planes[1].pixelStride)
        if (pixelStride == 2 && rowStride == width && uBuffer[0] == vBuffer[1]) {
            // maybe V an U planes overlap as per NV21, which means vBuffer[1] is alias of uBuffer[0]
            val savePixel = vBuffer[1]
            try {
                vBuffer.put(1, savePixel.inv() as Byte)
                if (uBuffer[0] == savePixel.inv() as Byte) {
                    vBuffer.put(1, savePixel)
                    vBuffer.position(0)
                    uBuffer.position(0)
                    vBuffer[nv21, ySize, 1]
                    uBuffer[nv21, ySize + 1, uBuffer.remaining()]
                    return nv21 // shortcut
                }
            } catch (ex: ReadOnlyBufferException) {
                // unfortunately, we cannot check if vBuffer and uBuffer overlap
            }

            // unfortunately, the check failed. We must save U and V pixel by pixel
            vBuffer.put(1, savePixel)
        }

        // other optimizations could check if (pixelStride == 1) or (pixelStride == 2),
        // but performance gain would be less significant
        for (row in 0 until height / 2) {
            for (col in 0 until width / 2) {
                val vuPos = col * pixelStride + row * rowStride
                nv21[pos++] = vBuffer[vuPos]
                nv21[pos++] = uBuffer[vuPos]
            }
        }
        return nv21
    }


    fun checkImageOrientation(bitmap: Bitmap): Bitmap {

        var angle = 0
        val ei = ExifInterface(imagePath)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        var rotatedBitmap: Bitmap? = null
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                angle = 90
                rotatedBitmap =
                    rotateImage(bitmap, angle.toFloat())
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                angle = 180
                rotatedBitmap =
                    rotateImage(bitmap, angle.toFloat())
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                angle = 270
                rotatedBitmap =
                    rotateImage(bitmap, angle.toFloat())
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

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    fun StringToBitMap(base64: String?): Bitmap? {
        return try {
            val imageBytes = Base64.decode(base64, 0)
            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            image
        }catch (e: Exception){
            Log.d("StringToBitMap","${e.localizedMessage}")
            null
        }
    }




//fun bitmpConvert(encodedImage: String?):Bitmap{
//    return try {
//        val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
//        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//    }
//
//}
}