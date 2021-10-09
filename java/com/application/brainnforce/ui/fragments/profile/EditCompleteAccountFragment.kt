package com.application.brainnforce.ui.fragments.auth

import android.Manifest
import android.annotation.SuppressLint
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.application.brainnforce.R
import com.application.brainnforce.databinding.FragCompleteAccountBinding
import com.application.brainnforce.model.*
import com.application.brainnforce.ui.fragments.category.EditCategoryFragment
import com.application.brainnforce.ui.fragments.home.DashboardFragment
import com.application.brainnforce.ui.fragments.home.HomeFragment
import com.application.brainnforce.ui.fragments.profile.EditProfileFragment
import com.application.brainnforce.ui.fragments.profile.ProfileFragment
import com.application.brainnforce.ui.fragments.profile.SellerProfileFragment
import com.application.brainnforce.utils.content
import com.application.brainnforce.utils.video_compressor.VideoCompress
import com.application.swapp.app.BNFBaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import gun0912.tedbottompicker.TedBottomPicker
import gun0912.tedbottompicker.adapter.GalleryAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.reflect.Type

@Suppress(
    "INACCESSIBLE_TYPE", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION"
)
class EditCompleteAccountFragment : BNFBaseFragment<FragCompleteAccountBinding>() {

    private var data: SignUpSellerRequestModel1? = null
    var videoPaths: File? = null
    var listContact: List<SubCategoriesList>? = null
    var stringList = ""
    private var user: User? = null

    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun isBottomBarVisible(): Boolean {
        return false
    }

    @SuppressLint("UseRequireInsteadOfGet", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (user == null) {

        user = linkodesPref.retrieveUserInfo(getContainerActivity())!!

        viewDataBinding.experience.setText(user!!.totalExperience)
        viewDataBinding.languageName.setText(user!!.languages)

        viewDataBinding.backClick.setOnClickListener {
            getContainerActivity().onBackPressed()
        }

        viewDataBinding.createAccountBtn.setOnClickListener {
            if (validateEditText(viewDataBinding.experience) &&
                validateEditText(viewDataBinding.languageName)
            ) {
                callSellerAccountAPI()
            }
        }

        viewDataBinding.textCategory.setOnClickListener {
            displayIt(
                EditCategoryFragment(3),
                EditCategoryFragment::class.java.canonicalName,
                true
            )
        }

        viewDataBinding.uploadVideoId.setOnClickListener {

            getCameraPermissions()

        }

        viewDataBinding.uploadVideoId1.setOnClickListener {

            getCameraPermissions()

        }

        if (!linkodesPref.getSaveValue(getContainerActivity(), "Category").isBlank()) {
            stringList = linkodesPref.getSaveValue(getContainerActivity(), "Category")
            val type: Type = object : TypeToken<List<SubCategoriesList?>?>() {}.type
            listContact = Gson().fromJson(stringList, type)

            Log.e("SIZE", listContact?.size.toString())

            if (listContact?.size!! > 0) {
                viewDataBinding.textCategory.setText(listContact!!.size.toString() + " Categories Selected")
            } else {
                viewDataBinding.textCategory.setText(user!!.userCategoriesList!!.size.toString() + " Categories Selected")
            }
        } else {
            viewDataBinding.textCategory.setText(user!!.userCategoriesList!!.size.toString() + " Categories Selected")
        }

    }

    fun getCameraPermissions() {
        Dexter.withActivity(getContainerActivity())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                    when {
                        report.isAnyPermissionPermanentlyDenied -> {
                            openSettings()
                        }
                        report.areAllPermissionsGranted() -> {

                            openVideoPicker()

                        }
                        else -> openSettings()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun openVideoPicker() {
        TedBottomPicker.with(getContainerActivity())
            .showVideoMedia()
            .show {

                val videoPath = it.path.toString()

                if (GalleryAdapter.PickerTile.GALLERY == 3) {
                    if (calculateSizeOfSelectedFileInMB(videoPath) <= 30) {

                        executeCompression(videoPath)
                    } else {
                        showToast("uploaded video size must be less than 30MB.")
                    }
                }
                else {
                    executeCompression(videoPath)
                }

            }
    }

    fun calculateSizeOfSelectedFileInMB(fileName: String?): Long {
        val file = File(fileName)
        val length = file.length()
        val fileSizeInKB = length / 1024
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        return fileSizeInKB / 1024
    }

    fun executeCompression(videoPath: String) {
        var destPath = videoPath.substring(0, videoPath.lastIndexOf('.'))
        destPath += "Compressed.mp4"
        VideoCompress.compressVideoMedium(
            videoPath,
            destPath,
            object : VideoCompress.CompressListener {
                override fun onSuccess() {
                    val thumb = ThumbnailUtils.createVideoThumbnail(
                        destPath,
                        MediaStore.Video.Thumbnails.MINI_KIND
                    )
                    val video = File(destPath)
                    try {

                        Log.e(
                            "TAGGG", "" + video + "?" + File(
                                saveImageToSdCard(
                                    activity,
                                    thumb!!
                                )
                            )
                        )

                        videoPaths = video

                        viewDataBinding.uploadVideoId.visibility = View.GONE
                        viewDataBinding.uploadVideoId1.visibility = View.VISIBLE

                        setGlideString(
                            File(saveImageToSdCard(activity, thumb)).toString(),
                            viewDataBinding.uploadVideoId1
                        )

                    } catch (e: Exception) {
                        e.printStackTrace()
                        showToast("Something went wrong video thumbnail cannot be generated!!")
                    }

                }

                override fun onFail() {
                    //     homeActivity.stopProgressDialog()
                    Log.e("video>>", "failess")
                }

                override fun onProgress(percent: Float) {
                    Log.e("video>>>", "$percent%")

                }

                override fun onStart() {

                }

            })
    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_complete_account
    }

    private fun callSellerAccountAPI() {
        viewDataBinding.apply {
//            if (data == null) {

            data = linkodesPref.getUserData(getContainerActivity())!!

            val totalExperience = viewDataBinding?.experience?.content
            val languages = viewDataBinding?.languageName?.content

            val updateCommentData = SignUpSellerRequestModel()

            updateCommentData.firstname = data!!.firstname
            updateCommentData.lastname = data!!.lastname
            updateCommentData.gender = data!!.gender
            updateCommentData.dateofbirth = data!!.dateofbirth
            updateCommentData.mobile = data!!.mobile
            updateCommentData.email = data!!.email
            updateCommentData.location = data!!.location
            updateCommentData.latitude = data!!.latitude
            updateCommentData.longitude = data!!.longitude
            updateCommentData.totalExperience = totalExperience
            updateCommentData.subCategoriesList = listContact
            updateCommentData.languages = languages

            var profilePicture: MultipartBody.Part? = null
            var idProof: MultipartBody.Part? = null
            var coverVideo: MultipartBody.Part? = null


            if (data!!.profilePicture != null) {
                val profilePicturePath =
                    data!!.profilePicture!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                profilePicture = MultipartBody.Part.createFormData(
                    "profilePicture",
                    data!!.profilePicture!!.name,
                    profilePicturePath
                )
            }


            if (data!!.idProof != null) {
                val idProofPath =
                    data!!.idProof!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                idProof = MultipartBody.Part.createFormData(
                    "idProof",
                    data!!.idProof!!.name,
                    idProofPath
                )
            }


            if (videoPaths != null) {

                val coverVideoPath =
                    videoPaths!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                coverVideo = MultipartBody.Part.createFormData(
                    "coverVideo",
                    videoPaths!!.name,
                    coverVideoPath
                )
            }


            showLoading()
            disposable.add(
                apiService.createProfileSeller(
                    updateCommentData,
                    profilePicture,
                    idProof,
                    coverVideo
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Any>() {

                        override fun onSuccess(model: Any) {
                            hideLoading()
                            showToast("Account Edit Successfully")
                            getContainerActivity().supportFragmentManager.popBackStack(
                                EditProfileFragment::class.java.canonicalName,
                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                            )

                            displayIt(
                                SellerProfileFragment(),
                                SellerProfileFragment::class.java.canonicalName,
                                true
                            )

                        }

                        override fun onError(e: Throwable) {
                            hideLoading()
                        }
                    })
            )
        }
//        }
    }

}
