package com.example.augmanium.beforeAuth.services

import android.annotation.SuppressLint

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.hardware.camera2.CaptureRequest
import android.media.Image

import android.provider.ContactsContract.Intents.Insert.ACTION
import android.provider.ContactsContract.Intents.Insert.DATA
import android.util.Log
import android.util.Range
import android.util.Size
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.provider.FontsContractCompat.Columns.RESULT_CODE
import androidx.lifecycle.LifecycleService
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.nio.ReadOnlyBufferException
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.experimental.inv

class ScanFaceService: LifecycleService() {
    var TAG = "SCANFACE "
    var takePitureTimer = Timer()
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    var cameraProvider: ProcessCameraProvider? = null
    var cameraSelector: CameraSelector? = null

    companion object{
        fun getStartIntent(context: Context, resultCode: Int, data: Intent?):Intent{
            val intent = Intent(context, ScanFaceService::class.java)
            intent.putExtra(ACTION,"START" )
            intent.putExtra(RESULT_CODE, resultCode)
            intent.putExtra(DATA, data)
            return intent
        }

        fun getStopIntent(context: Context):Intent{
            val intent = Intent(context, ScanFaceService::class.java)
            intent.putExtra(ACTION, "STOP")

            return intent
        }

    }

    fun startCameraEvery4Minutes() {

        takePicHandler4sec()
        cameraBind()
    }

    var toTakePic = false
    var secondsElapsed = 1
    private fun takePicHandler4sec() {

        takePitureTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                toTakePic = true;
            }
        }, 0, 1000)
    }

    private fun cameraBind() {
        Log.e("CameraBind", "Creating an instance")
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture!!.addListener({
            try {
                cameraProvider = cameraProviderFuture!!.get()
                bindPreview(cameraProvider!!)
            } catch (e: ExecutionException) {
                // No errors need to be handled for this in Future.
                // This should never be reached.
            } catch (e: InterruptedException) {
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {

        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        val builder = ImageAnalysis.Builder()
        val ext: Camera2Interop.Extender<*> = Camera2Interop.Extender(builder)

        ext.setCaptureRequestOption(
            CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE,
            Range<Int>(1, 1)
        )
        val executor: Executor = Executors.newSingleThreadExecutor()
        val imageAnalysis = builder
            .setTargetResolution(Size(300, 350))
            .setBackgroundExecutor(executor)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) //Latest frame is shown
            .build()

        imageAnalysis.setAnalyzer(executor) { imageProxy ->

            CoroutineScope(Dispatchers.IO).launch {

                if (toTakePic) {
                    toTakePic = false
                    val mediaImage = imageProxy.image
                    println("ANALYSIS")

                    try {
                        if (mediaImage != null) {
                            processImage(mediaImage!!)
                        } else {
                            Log.wtf(TAG, "Image is null");
                        }

                    } catch (e: InterruptedException) {
                        Log.wtf(TAG, "Image Analysis Was Interrupted " + e.localizedMessage)
                    }
                }

                imageProxy.close()
            }

            //Process acquired image to detect faces


        }
        //try catch added -----ARBAZ------
        try {
//            val owner = activityContext as LifecycleOwner
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector!!, imageAnalysis)
        } catch (e: Exception) {
            Log.wtf(TAG, "Exception on bindPreview ${e.localizedMessage}")
        }

    }


    private fun processImage(image: Image) {
        Log.wtf(TAG, "Inside Process Image Function.")
//        val checkFaceRecognition = compareFaceCheckLoggedInMethod()
//        if (checkFaceRecognition || isStreamEnabled || isMonitorEnabled || isRecordingEnabled) {

            val bitmap = toBitmap(image)
            if (bitmap != null) {
                CoroutineScope(Job()).launch(Dispatchers.IO) {

                    val rotatedBitmap = bitmap.rotate(270f)
//                    if (isStreamEnabled || isMonitorEnabled || isRecordingEnabled) {
//
//                        Log.wtf("Lag_Issue", "Function Called")
//                        saveImageToDisk(
//                            rotatedBitmap.copy(rotatedBitmap.config, true)
//                        )
//
//
//                    }

                    // val resizedBitmap = rotatedBitmap.resizeForVideo(900, 2040)
                    Log.wtf(TAG, "Image saved.")
//                    if (checkFaceRecognition) {


                        // Save if any face cheating is in propgress
//
//                        val resizedBitmap = rotatedBitmap.resizeForVideo(320, 480)
//                        secondsElapsed++
//                        if (secondsElapsed >= 2) {
//                            // For Face Cheat
//                            compareImages(rotatedBitmap.copy(rotatedBitmap.config, true))
//                            secondsElapsed = 0;
//                            saveFaceCheatingImage(resizedBitmap.copy(resizedBitmap.config, true))
//                        }
//                        resizedBitmap.recycle()

//                    }


                    // Recycle ALl
                    rotatedBitmap.recycle()
                    bitmap.recycle()
                    image.close()
                }
            }

    }


    private fun toBitmap(image: Image): Bitmap? {
        Log.wtf(TAG, "TO_BITMAP_IMAGE $image")
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
    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    fun YUV_420_888toNV21(image: Image): ByteArray? {
        Log.wtf(TAG, "YUV_420_IMAGE $image")
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



    override fun onDestroy() {
        super.onDestroy()
        takePitureTimer.cancel()
        takePitureTimer.purge()
    }
}