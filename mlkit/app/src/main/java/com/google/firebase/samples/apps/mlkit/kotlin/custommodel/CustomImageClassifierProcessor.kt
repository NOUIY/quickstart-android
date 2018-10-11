package com.google.firebase.samples.apps.mlkit.kotlin.custommodel

import android.app.Activity
import android.graphics.Bitmap
import android.media.Image
import com.google.firebase.ml.common.FirebaseMLException
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.samples.apps.mlkit.common.*
import java.nio.ByteBuffer

/** Custom Image Classifier Demo.  */
class CustomImageClassifierProcessor @Throws(FirebaseMLException::class)
constructor(private val activity: Activity) : VisionImageProcessor {

    private val classifier: CustomImageClassifier

    init {
        classifier = CustomImageClassifier(activity)
    }

    @Throws(FirebaseMLException::class)
    override fun process(
        data: ByteBuffer,
        frameMetadata: FrameMetadata,
        graphicOverlay: GraphicOverlay
    ) {
        classifier
            .classifyFrame(data, frameMetadata.width, frameMetadata.height)
            .addOnSuccessListener(
                activity
            ) { result ->
                val labelGraphic = LabelGraphic(graphicOverlay, result)
                val bitmap = BitmapUtils.getBitmap(data, frameMetadata)
                val imageGraphic = CameraImageGraphic(graphicOverlay, bitmap)
                graphicOverlay.clear()
                graphicOverlay.add(imageGraphic)
                graphicOverlay.add(labelGraphic)
                graphicOverlay.postInvalidate()
            }
    }

    override fun process(bitmap: Bitmap, graphicOverlay: GraphicOverlay) {
        // nop
    }

    override fun stop() {}
}