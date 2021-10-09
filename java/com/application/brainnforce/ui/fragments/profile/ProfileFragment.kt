package com.application.brainnforce.ui.fragments.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.application.brainnforce.R
import com.application.brainnforce.common.BNFConstants
import com.application.brainnforce.databinding.FragProfileBinding
import com.application.brainnforce.model.LoginCrediental
import com.application.brainnforce.ui.fragments.auth.ChooseAccountTypeFragment
import com.application.brainnforce.ui.fragments.category.DisputesFragment
import com.application.brainnforce.ui.fragments.conversation.VideoSubscriptionFragment
import com.application.brainnforce.ui.fragments.home.HomeFragment
import com.application.brainnforce.ui.fragments.notification.BlockUserFragment
import com.application.brainnforce.ui.fragments.payment.PaymentSettingFragment
import com.application.brainnforce.ui.fragments.setting.ChangePasswordFragment
import com.application.swapp.app.BNFBaseFragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
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


@Suppress("DEPRECATION")
class ProfileFragment : BNFBaseFragment<FragProfileBinding>() {
    var coverPaths: File? = null
    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_profile
    }

    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun isBottomBarVisible(): Boolean {
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val acct = GoogleSignIn.getLastSignedInAccount(activity)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(getContainerActivity(), gso)

        if (acct != null) {
            val personName = acct?.displayName
            val personEmail = acct?.email
            val personId = acct?.id
            val personPhoto: Uri? = acct?.photoUrl

            viewDataBinding.profileFullname.setText(personName)
            viewDataBinding.textViewName.setText(personName)
            viewDataBinding.profileEmail.setText(personEmail)
            Glide.with(this).load(acct!!.photoUrl).into(viewDataBinding.imgBackProfile)
            Glide.with(this).load(acct!!.photoUrl).into(viewDataBinding.circularImageView)


        } else {
            callGetUserInfo()
        }


        viewDataBinding.cameraIcon.setOnClickListener {
            getCameraPermissions()
        }


        viewDataBinding.raiseDisputeId.setOnClickListener {
            displayIt(DisputesFragment(), DisputesFragment::class.java.canonicalName, true)
        }

        viewDataBinding.changePasswordId.setOnClickListener {
            displayIt(
                ChangePasswordFragment(),
                ChangePasswordFragment::class.java.canonicalName,
                true
            )
        }

        viewDataBinding.payment.setOnClickListener {
            displayIt(
                PaymentSettingFragment(),
                PaymentSettingFragment::class.java.canonicalName,
                true
            )
        }



        viewDataBinding.blockedUserid.setOnClickListener {
            displayIt(BlockUserFragment(), BlockUserFragment::class.java.canonicalName, true)
        }

        viewDataBinding.videoSubs.setOnClickListener {
            displayIt(
                VideoSubscriptionFragment(),
                VideoSubscriptionFragment::class.java.canonicalName,
                true
            )
        }

        viewDataBinding.editImageView.setOnClickListener {
            displayIt(
                EditProfileFragment(),
                EditProfileFragment::class.java.canonicalName,
                true
            )
        }

        viewDataBinding.logoutID.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            googleSignInClient.signOut()
            linkodesPref.clearPrefs(getContainerActivity())
            linkodesPref.setUserLoggedInStatus(getContainerActivity(), false)
            getContainerActivity().supportFragmentManager.popBackStack(
                HomeFragment::class.java.canonicalName,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            displayIt(
                ChooseAccountTypeFragment(1),
                ChooseAccountTypeFragment::class.java.canonicalName,
                false
            )
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
                            viewDataBinding.circularImageView
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

                        viewDataBinding.textViewName.text =
                            model.data!!.user!!.firstname + " " + model.data!!.user!!.lastname
                        viewDataBinding.textViewGender.text = model.data!!.user!!.gender
                        viewDataBinding.profileFullname.setText(model.data!!.user!!.firstname + " " + model.data!!.user!!.lastname)
                        viewDataBinding.profileEmail.setText(model.data!!.user!!.email)
                        viewDataBinding.profileLocation.setText(model.data!!.user!!.location)
                        viewDataBinding.aboutProfile.setText(model.data!!.user!!.bio)

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