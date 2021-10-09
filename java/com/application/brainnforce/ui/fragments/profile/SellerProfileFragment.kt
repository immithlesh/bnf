package com.application.brainnforce.ui.fragments.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.common.BNFConstants
import com.application.brainnforce.databinding.FragSellerProfileBinding
import com.application.brainnforce.model.LoginCrediental
import com.application.brainnforce.ui.fragments.category.ViewPagerAdapter
import com.application.brainnforce.ui.fragments.setting.SettingFragment
import com.application.swapp.app.BNFBaseFragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Suppress("UNREACHABLE_CODE")
class SellerProfileFragment : BNFBaseFragment<FragSellerProfileBinding>() {

    var coverPaths: File? = null

    override fun getLayoutId(): Int {
        return R.layout.frag_seller_profile
    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun isBottomBarVisible(): Boolean {
        if (linkodesPref.getSaveValue(getContainerActivity(), "Account_Type") == "Buyer") {
            return false
        } else if (linkodesPref.getSaveValue(getContainerActivity(), "Account_Type") == "Seller") {
            return true
        }
        return null!!
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val acct = GoogleSignIn.getLastSignedInAccount(activity)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(getContainerActivity(), gso)

        if (acct != null) {
            val personName = acct?.displayName

            viewDataBinding.sellerName.setText(personName)
            Glide.with(this).load(acct!!.photoUrl).into(viewDataBinding.imgBackProfile)
            Glide.with(this).load(acct!!.photoUrl).into(viewDataBinding.sellerProfileImg)


        } else {
            callGetUserInfo()
            if (linkodesPref.getSaveValue(getContainerActivity(), "Account_Type") == "Buyer") {
                viewDataBinding.cameraIcon.visibility = View.GONE
                viewDataBinding.settingIcon.visibility = View.GONE
                viewDataBinding.editProfileView.background =
                    ContextCompat.getDrawable(getContainerActivity(), R.drawable.ic_share)
            } else if (linkodesPref.getSaveValue(
                    getContainerActivity(),
                    "Account_Type"
                ) == "Seller"
            ) {
                viewDataBinding.cameraIcon.visibility = View.VISIBLE
                viewDataBinding.settingIcon.visibility = View.VISIBLE
                viewDataBinding.editProfileView.background =
                    ContextCompat.getDrawable(getContainerActivity(), R.drawable.ic_edit)
                viewDataBinding.backIcon.visibility = View.GONE

                viewDataBinding.editProfileView.setOnClickListener {

                    linkodesPref.clear(getContainerActivity(), "Category")
                    displayIt(
                        EditProfileFragment(),
                        EditProfileFragment::class.java.canonicalName,
                        true
                    )
                }

            }


//        callGetUserInfo()


            viewDataBinding.cameraIcon.setOnClickListener {
                getCameraPermissions()
            }


        }


        val adapter =
            ViewPagerAdapter(
                childFragmentManager
            )

        viewDataBinding.backIcon.setOnClickListener {
            getContainerActivity().onBackPressed()
        }


        viewDataBinding.settingIcon.setOnClickListener {
            displayIt(SettingFragment(), SettingFragment::class.java.canonicalName, true)

        }

        adapter.addFragment(ProfileInformationFragment(), "Information")
        adapter.addFragment(ProfileServicesFragment(), "Services")


        viewDataBinding.sellerViewPager.adapter = adapter
        viewDataBinding.sellerTabLayout.setupWithViewPager(viewDataBinding.sellerViewPager)


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

                            getStorageAcceptedListener()

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

    fun getStorageAcceptedListener() {

        ImagePicker.with(getContainerActivity())
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )  //Final image resolution will be less than 1080 x 1080(Optional)
            .start { resultCode, data ->
                if (resultCode == Activity.RESULT_OK) {
                    val compressedImage = ImagePicker.getFile(data)!!
                    coverPaths = compressedImage
                    setGlideFile(compressedImage, viewDataBinding?.imgBackProfile)
                    callCoverInfo()
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    showToast(ImagePicker.getError(data))
                }
            }
    }

    private fun callGetUserInfo() {
        showLoading()
        disposable.add(
            apiService.apiGetUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginCrediental>() {
                    @SuppressLint("SetTextI18n")
                    override fun onSuccess(model: LoginCrediental) {
                        hideLoading()
                        setGlideString(
                            BNFConstants.IMG_URL + model.data!!.user!!.profileImagePath,
                            viewDataBinding.sellerProfileImg
                        )

                        if (model.data!!.user!!.coverImagePath != null) {
                            setGlideString(
                                BNFConstants.IMG_URL + model.data!!.user!!.coverImagePath,
                                viewDataBinding.imgBackProfile
                            )
                        } else {
                            setGlideString(
                                BNFConstants.IMG_URL + model.data!!.user!!.profileImagePath,
                                viewDataBinding.imgBackProfile
                            )
                        }

                        viewDataBinding.sellerName.text =
                            model.data!!.user!!.firstname + " " + model.data!!.user!!.lastname

                        linkodesPref.storeUserInfo(getContainerActivity(), model.data!!.user!!)

                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.d("logE", "" + e)
                    }
                })
        )
    }

    private fun callCoverInfo() {
        showLoading()

        var coverImage: MultipartBody.Part? = null

        val coverImagePath =
            coverPaths!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        coverImage = MultipartBody.Part.createFormData(
            "coverImage",
            coverPaths!!.name,
            coverImagePath
        )

        disposable.add(

            apiService.addCoverImage(coverImage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Any>() {

                    override fun onSuccess(model: Any) {
                        hideLoading()
                        showToast("Cover Image Successfully Changed")
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.d("logE", "" + e)
                    }
                })
        )
    }

}
