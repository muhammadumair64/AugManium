package com.example.augmanium

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import java.io.File
import java.io.IOException
import java.util.function.Consumer

class ModelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_model) FirebaseApp . initializeApp this
//
//        val storage: FirebaseStorage = FirebaseStorage.getInstance()
//        val modelRef: StorageReference = storage.getReference().child("out.glb")
//
//        val arFragment = supportFragmentManager
//            .findFragmentById(R.id.arFragment) as ArFragment?
//
//        findViewById<View>(R.id.downloadBtn)
//            .setOnClickListener { v: View? ->
//                try {
//                    val file = File.createTempFile("out", "glb")
//                    modelRef.getFile(file)
//                        .addOnSuccessListener(OnSuccessListener<Any?> { buildModel(file) })
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//
//        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane?, motionEvent: MotionEvent? ->
//            val anchorNode =
//                AnchorNode(hitResult.createAnchor())
//            anchorNode.renderable = renderable
//            arFragment.arSceneView.scene.addChild(anchorNode)
        }
    

    private var renderable: ModelRenderable? = null

//    private fun buildModel(file: File) {
//        val renderableSource: RenderableSource = RenderableSource
//            .builder()
//            .setSource(this, Uri.parse(file.path), RenderableSource.SourceType.GLB)
//            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
//            .build()
//        ModelRenderable
//            .builder()
//            .setSource(this, renderableSource)
//            .setRegistryId(file.path)
//            .build()
//            .thenAccept(Consumer { modelRenderable: ModelRenderable? ->
//                Toast.makeText(this, "Model built", Toast.LENGTH_SHORT).show()
//                renderable = modelRenderable
//            })
//    }
}
