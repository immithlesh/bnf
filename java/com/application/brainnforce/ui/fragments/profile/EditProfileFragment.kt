package com.application.brainnforce.ui.fragments.profile

import android.Manifest
import android.R.attr.path
import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.common.BNFConstants
import com.application.brainnforce.databinding.FragEditProfileBinding
import com.application.brainnforce.model.SignUpSellerRequestModel1
import com.application.brainnforce.model.User
import com.application.brainnforce.ui.fragments.auth.CompleteAccountFragment
import com.application.brainnforce.ui.fragments.auth.EditCompleteAccountFragment
import com.application.brainnforce.utils.content
import com.application.swapp.app.BNFBaseFragment
import com.github.dhaval2404.imagepicker.ImagePicker
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
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class EditProfileFragment : BNFBaseFragment<FragEditProfileBinding>() {
    var gender = "Male"
    var imgPaths: File? = null
    var idPaths: File? = null
    val myCalendar = Calendar.getInstance()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (user == null) {

            user = linkodesPref.retrieveUserInfo(getContainerActivity())!!

            if (linkodesPref.getSaveValue(getContainerActivity(), "Account_Type") == "Buyer") {
                viewDataBinding.createAccClick.text = getText(R.string.save)
                viewDataBinding.bioLayout.visibility = View.VISIBLE
            } else {
                viewDataBinding.createAccClick.text = getText(R.string.next)
                viewDataBinding.bioLayout.visibility = View.GONE
            }

            viewDataBinding.backClick.setOnClickListener {
                getContainerActivity().onBackPressed()
            }

            viewDataBinding.profileImgId.setOnClickListener {
                getCameraPermissions()
            }

            viewDataBinding.uploadClick.setOnClickListener {
                getUploadPermissions()
            }

            viewDataBinding.emailET.setFilters(arrayOf(EmojiExcludeFilter()))

            viewDataBinding.createAccClick.setOnClickListener {

                handleSignupAPIResponse()

            }

            gender = user!!.gender!!

            viewDataBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

                when (checkedId) {
                    R.id.rbMale -> {
                        gender = viewDataBinding.rbMale.text.toString()
                    }
                    R.id.rbFemale -> {
                        gender = viewDataBinding.rbFemale.text.toString()
                    }
                    R.id.rbOthers -> {
                        gender = viewDataBinding.rbOthers.text.toString()
                    }
                }
            }

            setGlideString(
                BNFConstants.IMG_URL + user!!.profileImagePath,
                viewDataBinding.profileImgId
            )

            viewDataBinding.uploadText.text = user!!.idProofPath

            viewDataBinding.firstNameET.setText(user!!.firstname)
            viewDataBinding.lastNameET.setText(user!!.lastname)
            viewDataBinding.dobET.setText(user!!.dateofbirth)
            viewDataBinding.phoneET.setText(user!!.mobile)
            viewDataBinding.locationET.setText(user!!.location)
            viewDataBinding.bioET.setText(user!!.bio)
            viewDataBinding.emailET.setText(user!!.email)

            if (gender.equals("Male")) {
                gender = "Male"
                viewDataBinding.rbMale.isChecked = true
            }
            else if (gender.equals("Other")) {
                gender = "Other"
                viewDataBinding.rbOthers.isChecked = true
            }
            else if (gender.equals("Female")) {
                gender = "Female"
                viewDataBinding.rbFemale.isChecked = true
            }
            val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                DateBFormat()
            }
            viewDataBinding.dobET.setOnClickListener {
                DatePickerDialog(
                    getContainerActivity(),
                    date,
                    myCalendar[Calendar.YEAR],
                    myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()

            }


        }

    }

    private fun DateBFormat() {
        val myFormat = "yyyy-MM-dd"
        //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        viewDataBinding.dobET.setText(sdf.format(myCalendar.getTime()))
    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_edit_profile
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

    fun getUploadPermissions() {
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

                            getUploadAcceptedListener()

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
                    imgPaths = compressedImage
                    setGlideFile(compressedImage, viewDataBinding?.profileImgId)
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    showToast(ImagePicker.getError(data))
                }
            }
    }

    fun getUploadAcceptedListener() {

        ImagePicker.with(getContainerActivity())
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )  //Final image resolution will be less than 1080 x 1080(Optional)
            .start { resultCode, data ->
                if (resultCode == Activity.RESULT_OK) {
                    val compressedImage = ImagePicker.getFile(data)!!
                    idPaths = compressedImage
                    viewDataBinding?.uploadText?.text = idPaths?.name
//                    setGlideFile(compressedImage, viewDataBinding?.uploadText)
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    showToast(ImagePicker.getError(data))
                }
            }
    }

    private fun handleSignupAPIResponse() {
        if (linkodesPref.getSaveValue(getContainerActivity(), "Account_Type") == "Buyer") {

            if (
                validateEditText(viewDataBinding.firstNameET) &&
                validateEditText(viewDataBinding.lastNameET) &&
                validateEditText(viewDataBinding.dobET) &&
                validateEditText(viewDataBinding.phoneET) &&
                validatePhoneNumber(viewDataBinding.phoneET) &&
                validateEditText(viewDataBinding.locationET) &&
                validateEditText(viewDataBinding.emailET) &&
                validateEditText(viewDataBinding.bioET) &&
                isEmailValid(viewDataBinding.emailET)
            ) {
                callBuyerAccountAPI()
            }
        } else {
            if (
                validateEditText(viewDataBinding.firstNameET) &&
                validateEditText(viewDataBinding.lastNameET) &&
                validateEditText(viewDataBinding.dobET) &&
                validateEditText(viewDataBinding.phoneET) &&
                validatePhoneNumber(viewDataBinding.phoneET) &&
                validateEditText(viewDataBinding.locationET) &&
                validateEditText(viewDataBinding.emailET) &&
                isEmailValid(viewDataBinding.emailET)
            ) {
                callSellerAccountAPI()
            }

        }

    }

    private fun callBuyerAccountAPI() {
        viewDataBinding.apply {

            val fname = firstNameET.content
            val lname = lastNameET.content
            val dob = dobET.content
            val email = emailET.content
            val locationName = locationET.content
            val mobileNum = phoneET.content
            val latt = user!!.latitude
            val longg = user!!.longitude
            val bio = bioET.content

            val json = JSONObject()
            json.put("firstname", fname)
            json.put("lastname", lname)
            json.put("gender", gender)
            json.put("dateofbirth", dob)
            json.put("mobile", mobileNum)
            json.put("email", email)
            json.put("location", locationName)
            json.put("latitude", latt)
            json.put("longitude", longg)
            json.put("bio", bio)

            val payload = json.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

            var profile: MultipartBody.Part? = null
            var id: MultipartBody.Part? = null

            if (imgPaths != null) {
                val requestFile =
                    imgPaths!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                profile = MultipartBody.Part.createFormData(
                    "profile",
                    imgPaths?.name,
                    requestFile
                )
            }

            if (idPaths != null) {
                val requestIDFile =
                    idPaths!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                id = MultipartBody.Part.createFormData(
                    "id",
                    idPaths?.name,
                    requestIDFile
                )
            }

            showLoading()
            disposable.add(
                apiService.createProfileBuyer(payload, profile, id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Any>() {

                        override fun onSuccess(model: Any) {
                            hideLoading()
                            showToast("Profile Edit Successfully")
                            getContainerActivity().onBackPressed()
                        }

                        override fun onError(e: Throwable) {
                            hideLoading()
                        }
                    })
            )
        }
    }


    private fun callSellerAccountAPI() {
        val dob = viewDataBinding?.dobET?.content
        val email = viewDataBinding?.emailET?.content
        val fname = viewDataBinding?.firstNameET?.content
        val lname = viewDataBinding?.lastNameET?.content
        val locationName = viewDataBinding?.locationET?.content
        val mobileNum = viewDataBinding?.phoneET?.content

        val signUpSellerRequestModel = SignUpSellerRequestModel1()
        /*signUpSellerRequestModel.firstname = "firstname"
        signUpSellerRequestModel.lastname = "lastname"
        signUpSellerRequestModel.gender = "male"
        signUpSellerRequestModel.dateofbirth = "1997-05-02"
        signUpSellerRequestModel.mobile = "1234567890"
        signUpSellerRequestModel.password = "123456"
        signUpSellerRequestModel.email = "email@mail.com"
        signUpSellerRequestModel.location = "mohali"
        signUpSellerRequestModel.latitude = "123456"
        signUpSellerRequestModel.longitude = "123456"
        signUpSellerRequestModel.profilePicture = imgPaths
        signUpSellerRequestModel.idProof = imgPaths*/

           signUpSellerRequestModel.firstname =  fname
           signUpSellerRequestModel.lastname =  lname
           signUpSellerRequestModel.gender = gender
           signUpSellerRequestModel.dateofbirth = dob
           signUpSellerRequestModel.mobile = mobileNum
           signUpSellerRequestModel.email = email
           signUpSellerRequestModel.location = locationName
           signUpSellerRequestModel.latitude = "123456"
           signUpSellerRequestModel.longitude = "123456"
           signUpSellerRequestModel.role_id = "2"
           signUpSellerRequestModel.profilePicture = imgPaths
           signUpSellerRequestModel.idProof = idPaths

        linkodesPref.storeUserData(getContainerActivity(), signUpSellerRequestModel)

        displayIt(
            EditCompleteAccountFragment(),
            EditCompleteAccountFragment::class.java.canonicalName,
            true
        )
    }


}