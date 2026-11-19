package com.genius.snapscribe.mlkit

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class TextRecognitionManager(
    private val recognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
) {

    /**
     * Extracts text from an in-memory Bitmap.
     */
    suspend fun recognizeTextFromBitmap(bitmap: Bitmap, rotationDegrees: Int = 0): Result<Text> {
        val image = InputImage.fromBitmap(bitmap, rotationDegrees)
        return processImage(image)
    }

    /**
     * Extracts text from an image URI (e.g., photo taken via Camera or selected from Gallery).
     */
    suspend fun recognizeTextFromUri(context: Context, imageUri: Uri): Result<Text> {
        return try {
            val image = InputImage.fromFilePath(context, imageUri)
            processImage(image)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun processImage(image: InputImage): Result<Text> =
        suspendCancellableCoroutine { continuation ->
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    continuation.resume(Result.success(visionText))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.failure(exception))
                }
        }

    fun close() {
        recognizer.close()
    }
}
