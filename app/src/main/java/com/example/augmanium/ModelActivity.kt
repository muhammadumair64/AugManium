package com.example.augmanium

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.util.function.Consumer




class ModelActivity : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    var renderable: ModelRenderable? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model)

        FirebaseApp.initializeApp(this)

        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val modelRef: StorageReference = storage.reference.child("macbook.glb")

        val arFragment = supportFragmentManager
            .findFragmentById(R.id.arFragment) as ArFragment?





        findViewById<View>(R.id.add_to_cart_layout)
            .setOnClickListener { v: View? ->
                try {
                    val file = File.createTempFile("macbook", "glb")
                    modelRef.getFile(file)
                        .addOnSuccessListener(OnSuccessListener<Any?> {

                            buildModel(file)

                        })
                } catch (e: IOException) {
                    Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane?, motionEvent: MotionEvent? ->
            val anchorNode =
                AnchorNode(hitResult.createAnchor())
            renderable
            anchorNode.renderable = renderable
            arFragment.arSceneView.scene.addChild(anchorNode)
        }

    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun buildModel(file: File) {
        val renderableSource: RenderableSource = RenderableSource
            .builder()
            .setSource(this, Uri.parse(file.path), RenderableSource.SourceType.GLB)
            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ModelRenderable
                .builder()
                .setSource(this, renderableSource)
                .setRegistryId(file.path)
                .build()
                .thenAccept(Consumer { modelRenderable: ModelRenderable? ->
                    Toast.makeText(this, "Model built", Toast.LENGTH_SHORT).show()
                    renderable = modelRenderable
                })
        }
    }

}
