package com.application.brainnforce.utils

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import com.application.brainnforce.BrainNForceApplication
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileNotFoundException
import java.util.*


fun String?.createMultipartBody(fieldName: String): MultipartBody.Part? {
        if (isNullOrEmpty()) {
            return null
        }
        var file: File? = null
        try {
            file = File(this)
            val requestBody = file.let { createRequestBody(it) }
            if (requestBody != null)
                return MultipartBody.Part.createFormData(fieldName, file.name, requestBody)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    private fun createRequestBody(file: File): RequestBody? {
        val mime = getMimeType(Uri.fromFile(file))
        if (mime != null) {
            return file.asRequestBody(mime.toMediaTypeOrNull())
        }
        return null
    }


    fun String.getRequestBody(): RequestBody {
        return toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    fun Int.getRequestBody(): RequestBody {
        return this.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }


    fun getMimeType(uri: Uri): String? {
        var mimeType: String? = null
        if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            val cr = BrainNForceApplication.appContext?.contentResolver
            mimeType = cr?.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
                uri
                    .toString()
            )
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase(Locale.ROOT)
            )
        }
        return mimeType
    }



