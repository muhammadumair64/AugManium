package com.example.augmanium

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.augmanium.utils.K
import com.example.augmanium.utils.TinyDB
import com.google.android.gms.tasks.OnSuccessListener
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment.OnTapArPlaneListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.util.function.Consumer


class ModelActivity : AppCompatActivity() {


    lateinit var tinyDB: TinyDB

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model)
        tinyDB = TinyDB(this)
        FirebaseApp.initializeApp(this)
        val storage = FirebaseStorage.getInstance()
        val modalName = tinyDB.getString(K.MODEL)
        val modelRef = storage.reference.child("$modalName.glb")
        val arFragment = supportFragmentManager
            .findFragmentById(R.id.arFragment) as ArFragment?
        findViewById<View>(R.id.add_to_cart_layout)
            .setOnClickListener { v: View? ->
                try {
                    val file = File.createTempFile("$modalName", "glb")
                    modelRef.getFile(file).addOnSuccessListener { buildModel(file) }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane?, motionEvent: MotionEvent? ->
            val anchorNode = AnchorNode(hitResult.createAnchor())
            anchorNode.renderable = renderable
            arFragment.arSceneView.scene.addChild(anchorNode)
        }
    }

    private var renderable: ModelRenderable? = null

    @RequiresApi(Build.VERSION_CODES.N)
    private fun buildModel(file: File) {
        val renderableSource = RenderableSource
            .builder()
            .setSource(this, Uri.parse(file.path), RenderableSource.SourceType.GLB)
            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
            .build()
        ModelRenderable
            .builder()
            .setSource(this, renderableSource)
            .setRegistryId(file.path)
            .build()
            .thenAccept { modelRenderable: ModelRenderable? ->
                Toast.makeText(this, "Model built", Toast.LENGTH_SHORT).show()
                renderable = modelRenderable
            }
    }


}
