//package com.dluong.pet.data.worker
//
//import android.content.Context
//import androidx.core.net.toUri
//import androidx.hilt.work.HiltWorker
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import dagger.assisted.Assisted
//import timber.log.Timber
//
//@HiltWorker
//class LikeWorker(
//    @Assisted appContext: Context,
//    @Assisted workerParams: WorkerParameters,
//) : CoroutineWorker(appContext, workerParams) {
//
//    override suspend fun doWork(): Result {
//        val imageUriString = inputData.getString("image_uri")
//        if (imageUriString.isNullOrEmpty()) {
//            return Result.failure()
//        }
//        val imageUri = imageUriString.toUri()
//        try {
//            applicationContext.contentResolver.openInputStream(imageUri)?.use { inputStream ->
//                // Thực hiện upload hoặc xử lý ảnh
//                Timber.d("Processing image upload from URI: $imageUri")
//                // Giả sử thành công
//                return Result.success()
//            } ?: return Result.failure()
//        } catch (e: Exception) {
//            Timber.e(e, "Upload failed")
//            return Result.retry()
//        }
//    }
//}
