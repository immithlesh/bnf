package com.application.swapp.app

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.application.brainnforce.R
import com.application.brainnforce.app.BrainNForceActivity
import com.application.brainnforce.app.FragmentView
import com.application.brainnforce.base.BaseFragment
import com.application.brainnforce.common.BNFCommon
import com.application.brainnforce.common.CustomDialog
import com.application.brainnforce.common.L
import com.application.brainnforce.databinding.*
import com.application.brainnforce.listner.PermissionsListener
import com.application.brainnforce.ui.fragments.conversation.ChatFragment
import com.application.brainnforce.ui.fragments.payment.PaymentFragment
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
abstract class BNFBaseFragment<T : ViewDataBinding?> : BaseFragment<T>(), FragmentView {

    var maxX = 0
    var checkPermission: PermissionsListener? = null
    private lateinit var mContainerActivity: BrainNForceActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContainerActivity = context as BrainNForceActivity
    }

    fun getContainerActivity(): BrainNForceActivity {
        return mContainerActivity
    }


    fun getTextRequestBodyParams(value: String): RequestBody {
        return value.toRequestBody("text/form-data".toMediaTypeOrNull())
    }



    fun isEmptyFile(path: String?, key: String): MultipartBody.Part? {
        var imageprofile_body: MultipartBody.Part? = null
        imageprofile_body = if (path.isNullOrEmpty()) {
            MultipartBody.Part.createFormData(
                key,
                "",
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            )
        } else {
            val file = File(path)

            MultipartBody.Part.createFormData(
                key,
                file.name,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            )
        }
        return imageprofile_body
    }

    fun getFileRequestBodyParams(value: File): RequestBody {
        return value.asRequestBody("image/jpeg".toMediaTypeOrNull())
    }


    override fun onStart() {
        super.onStart()
        mContainerActivity.getCurrentFragment(getCurrentFragment())
        mContainerActivity.isToolbarVisible(isToolbarVisible())
        mContainerActivity.isBottomBarVisible(isBottomBarVisible())
        mContainerActivity.configureToolbar(configureToolbar())
    }

    /* Validate EditText that checks empty. */
    fun validateEditText(et: EditText): Boolean {

        return if (et.text.toString() == "") {
            et.requestFocus()
            et.error = "This field can't be empty"
            false
        } else
            true
    }

    /* Validate EditText that checks empty. */
    fun validatePasswordEditText(et: EditText): Boolean {

        return if (et.text.toString() == "") {
            et.requestFocus()
            et.error = "This field can't be empty"
            false
        }else if (et.text.toString().length < 6) {
            et.requestFocus()
            et.error = "Password should be atleast 6 character"
            false
        }
        else
            true
    }

    /* Checks entered email is valid or not. */
    fun isEmailValid(et: EditText): Boolean {
        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(et.text.toString()).matches()

        return if (isValid) {
            true
        } else {
            et.requestFocus()
            et.error = "Entered email address is not valid"
            false
        }
    }

      class EmojiExcludeFilter : InputFilter {
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            for (i in start until end) {
                val type = Character.getType(source[i])
                if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                    return ""
                }
            }
            return null
        }
    }


    /* Checks both passwords are same or not. */
    fun comparePassword(etOne: EditText, etTwo: EditText): Boolean {

        if (etOne.text.toString() == etTwo.text.toString()) {
            return true
        } else {
            etTwo.requestFocus()
            etTwo.error = "Password and Confirm password doesn't match"
            return false
        }
    }

    fun notWithSpace(et: EditText) {
        val myWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (et.text.toString() == " ")
                    et.setText("")
            }
        }
        et.addTextChangedListener(myWatcher)
    }

    fun validatePhoneNumber(et: EditText): Boolean {

        return if (et.text.toString().length < 7 ||et.text.toString().length > 14) {
            et.requestFocus()
            et.error = "Entered phone number is not valid"
            false
        } else
            true
    }

    fun showError(e: Throwable, activity: AppCompatActivity) {

        var isNetworkException: Boolean

        try {
            val obj = JSONObject((e as HttpException).response().errorBody()!!.string())
            val error = obj.optString("message")
            val code = e.code()

            isNetworkException = false

            L.e("Error Message", "$error $code")

            if (code == 401) {

//                linkodesPref.clearPrefs(getContainerActivity())
//                displayIt(WelcomeFragment(), WelcomeFragment::class.java.canonicalName, true)
//                fragmentManager?.popBackStackImmediate(
//                    null,
//                    FragmentManager.POP_BACK_STACK_INCLUSIVE
//                )

            } else {
                BNFCommon.setResponseDialog(activity, error)
            }


        } catch (e: Exception) {
            isNetworkException = true
            e.printStackTrace()
        }

        L.e("TAG", "onError: " + e.message)
        if (isNetworkException) {

            if (e.message!!.contains("Failed to connect") || e.message!!.contains("Unable to resolve"))
                BNFCommon.setResponseDialog(
                    activity,
                    "Internet ERROR"
                )
            else
                BNFCommon.setResponseDialog(activity, e.message!!)

        }
//            LinkodesCommon.setResponseDialog(activity, e.message!!)
    }

    fun getPhoneNumber(number: String): String {
        val input = number
        return input.replace(" ", "")
    }

    fun commonDOBSelection(tv: EditText, userDOB: OnSelectedDOB, isMaxReq: Boolean) {

        val c = Calendar.getInstance()

        val mYear: Int
        val mMonth: Int
        val mDay: Int

        if (tv.text.toString().isNotEmpty()) {
            L.e("Filled DOB", tv.text.toString())

            c.timeInMillis = BNFCommon.getDateInMillies(tv.text.toString())

            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)

        } else {

            if (isMaxReq)
                mYear = c.get(Calendar.YEAR) - 16
            else
                mYear = c.get(Calendar.YEAR)

            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)

        }

//        val c = Calendar.getInstance()
//
//        val mYear = c.get(Calendar.YEAR) - 16
//        val mMonth = c.get(Calendar.MONTH)
//        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            getContainerActivity(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                val calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth)

                val formatMain = SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)
                tv.setText(formatMain.format(calendar.time))

                val apiFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                userDOB.selectedDate(apiFormat.format(calendar.time))


            },
            mYear,
            mMonth,
            mDay
        )

        if (isMaxReq)
            datePickerDialog.datePicker.maxDate =
                (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 16)).toLong()
        else
            datePickerDialog.datePicker.maxDate = (System.currentTimeMillis() - 1000)

        datePickerDialog.show()
    }

    interface OnSelectedDOB {
        fun selectedDate(date: String)
    }

    fun validateUneditableET(et: EditText, msg: String): Boolean {
        when {
            et.text.toString().isEmpty() -> {
                BNFCommon.setResponseDialog(
                    getContainerActivity(),
                    msg
                )
                return false
            }
            else -> return true
        }

    }


    interface OnDialogListeners {
        fun onYesClick(s: String)
    }

    interface OnDialogListener {
        fun onYesClick()
    }


    fun callFilterDialog(listener: OnDialogListener) {

        val binding = DataBindingUtil
            .inflate<FragFilterDialogBinding>(
                LayoutInflater.from(context), R.layout.frag_filter_dialog, null, false
            )


        val dialogFeed = CustomDialog.CustomDialog(getContainerActivity(), R.style.mytheme)
        dialogFeed.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFeed.setCancelable(true)
        dialogFeed.setContentView(binding.root)

        dialogFeed.show()

        binding.apply {

            btnApply.setOnClickListener {
                dialogFeed.dismiss()
                listener.onYesClick()
            }
            btnReset.setOnClickListener {
                dialogFeed.dismiss()
                listener.onYesClick()
            }
            closeId.setOnClickListener {
                dialogFeed.dismiss()
            }
        }

    }


    fun callVideoScheduledDialog(listener: OnDialogListeners) {

        val binding = DataBindingUtil
            .inflate<MainDialogAlertBinding>(
                LayoutInflater.from(context), R.layout.main_dialog_alert, null, false
            )

        val dialogFeed = CustomDialog.CustomDialog(getContainerActivity(), R.style.mytheme)
        dialogFeed.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFeed.setCancelable(true)
        dialogFeed.setContentView(binding.root)

        dialogFeed.show()

        binding.apply {


            videoConferencingId.setOnClickListener {
                dialogFeed.dismiss()
                listener.onYesClick("video")

            }
            reportId.setOnClickListener {
                dialogFeed.dismiss()
                listener.onYesClick("report")
            }
            blockId.setOnClickListener {
                dialogFeed.dismiss()
            }

            cancelId.setOnClickListener {
                dialogFeed.dismiss()
            }


        }
    }

    fun callReportDialog(listener: OnDialogListener) {

        val binding = DataBindingUtil
            .inflate<ReportDialogFragmentBinding>(
                LayoutInflater.from(context), R.layout.report__dialog_fragment, null, false
            )


        val dialogFeed = CustomDialog.CustomDialog(getContainerActivity(), R.style.mytheme)
        dialogFeed.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFeed.setCancelable(true)
        dialogFeed.setContentView(binding.root)

        dialogFeed.show()

        binding.apply {


            submitReport.setOnClickListener {
                dialogFeed.dismiss()
                listener.onYesClick()
            }
            closeId.setOnClickListener {
                dialogFeed.dismiss()
            }


        }

    }

    fun callVideoConferenceScheduledDialog(listener: OnDialogListener) {

        val binding = DataBindingUtil
            .inflate<VideoConferencingDialogBinding>(
                LayoutInflater.from(context), R.layout.video_conferencing_dialog, null, false
            )


        val dialogFeed = CustomDialog.CustomDialog(getContainerActivity(), R.style.mytheme)
        dialogFeed.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFeed.setCancelable(true)
        dialogFeed.setContentView(binding.root)

        dialogFeed.show()

        binding.apply {

            closeId.setOnClickListener {
                dialogFeed.dismiss()
            }


        }

    }

    fun callVideoDialog(listener: OnDialogListener) {

        val binding = DataBindingUtil
            .inflate<VideoDialogBinding>(
                LayoutInflater.from(context), R.layout.video_dialog, null, false
            )


        val dialogFeed = CustomDialog.CustomDialog(getContainerActivity(), R.style.mytheme)
        dialogFeed.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFeed.setCancelable(true)
        dialogFeed.setContentView(binding.root)

        dialogFeed.show()

        binding.apply {

            closeId.setOnClickListener {
                dialogFeed.dismiss()
            }

            monthlyClick.setOnClickListener {

                monthlyOutline.background =
                    ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline)
//            viewDataBinding.monthlyClick.background =
//                ContextCompat.getDrawable(getContainerActivity(), R.color.white)
                monthlyTxt.setTextColor(
                    ContextCompat.getColor(
                        getContainerActivity(),
                        R.color.purple
                    )
                )
//                monthlyImg.setImageResource(R.drawable.mothly_selected)
                monthlySelected.visibility = View.VISIBLE

                // unselected
                yearlyOutline.background =
                    ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline)
//            viewDataBinding.yearlyClick.background =
//                ContextCompat.getDrawable(getContainerActivity(), R.color.light_grey)
                yearlyTxt.setTextColor(
                    ContextCompat.getColor(
                        getContainerActivity(),
                        R.color.dark_grey
                    )
                )
//                yearlyImg.setImageResource(R.drawable.diamond)
                yearlySelected.visibility = View.INVISIBLE



                Handler().postDelayed({
                    dialogFeed.dismiss()
                    displayIt(ChatFragment(), ChatFragment::class.java.canonicalName, true)
                }, 1000)
            }

            yearlyClick.setOnClickListener {

                yearlyOutline.background =
                    ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline)
//            viewDataBinding.yearlyClick.background =
//                ContextCompat.getDrawable(getContainerActivity(), R.color.white)
                yearlyTxt.setTextColor(
                    ContextCompat.getColor(
                        getContainerActivity(),
                        R.color.purple
                    )
                )
//                yearlyImg.setImageResource(R.drawable.selected_diamond)
                yearlySelected.visibility = View.VISIBLE

                // unselected
                monthlyOutline.background =
                    ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline)
//            viewDataBinding.monthlyClick.background =
//                ContextCompat.getDrawable(getContainerActivity(), R.color.light_grey)
                monthlyTxt.setTextColor(
                    ContextCompat.getColor(
                        getContainerActivity(),
                        R.color.dark_grey
                    )
                )
//                monthlyImg.setImageResource(R.drawable.mothly)
                monthlySelected.visibility = View.INVISIBLE



                Handler().postDelayed({
                    dialogFeed.dismiss()
                    callVideoConferenceDialog(object : OnDialogListener {

                        override fun onYesClick() {
                            dialogFeed.dismiss()
                        }

                    })
                }, 1000)

            }


        }

    }

    fun callVideoConferenceDialog(listener: OnDialogListener) {

        val binding = DataBindingUtil
            .inflate<VideoConferenceDialogBinding>(
                LayoutInflater.from(context), R.layout.video_conference_dialog, null, false
            )


        val dialogFeed = CustomDialog.CustomDialog(getContainerActivity(), R.style.mytheme)
        dialogFeed.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFeed.setCancelable(true)
        dialogFeed.setContentView(binding.root)

        dialogFeed.show()

        binding.apply {

            closeId.setOnClickListener {
                dialogFeed.dismiss()
            }

            monthlyClick.setOnClickListener {

                monthlyOutline.background =
                    ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline)
//            viewDataBinding.monthlyClick.background =
//                ContextCompat.getDrawable(getContainerActivity(), R.color.white)
                monthlyText.setTextColor(
                    ContextCompat.getColor(
                        getContainerActivity(),
                        R.color.purple
                    )
                )
                monthlyImg.setImageResource(R.drawable.select_monthly)
                monthlySelected.visibility = View.VISIBLE

                // unselected
                yearlyOutline.background =
                    ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline)
//            viewDataBinding.yearlyClick.background =
//                ContextCompat.getDrawable(getContainerActivity(), R.color.light_grey)
                yearlyText.setTextColor(
                    ContextCompat.getColor(
                        getContainerActivity(),
                        R.color.dark_grey
                    )
                )
                yearlyImg.setImageResource(R.drawable.unselect_total_earning)
                yearlySelected.visibility = View.INVISIBLE


            }

            yearlyClick.setOnClickListener {

                yearlyOutline.background =
                    ContextCompat.getDrawable(getContainerActivity(), R.drawable.blue_outline)
//            viewDataBinding.yearlyClick.background =
//                ContextCompat.getDrawable(getContainerActivity(), R.color.white)
                yearlyText.setTextColor(
                    ContextCompat.getColor(
                        getContainerActivity(),
                        R.color.purple
                    )
                )
                yearlyImg.setImageResource(R.drawable.select_total_earning)
                yearlySelected.visibility = View.VISIBLE

                // unselected
                monthlyOutline.background =
                    ContextCompat.getDrawable(getContainerActivity(), R.drawable.grey_outline)
//            viewDataBinding.monthlyClick.background =
//                ContextCompat.getDrawable(getContainerActivity(), R.color.light_grey)
                monthlyText.setTextColor(
                    ContextCompat.getColor(
                        getContainerActivity(),
                        R.color.dark_grey
                    )
                )
                monthlyImg.setImageResource(R.drawable.unselect_monthly)
                monthlySelected.visibility = View.INVISIBLE


            }


            ContactFreelancerNBtn.setOnClickListener {
                displayIt(PaymentFragment(), PaymentFragment::class.java.canonicalName, true)
                dialogFeed.dismiss()
            }


        }

    }
//
//    fun isEmptyFile(path: String?, key: String): MultipartBody.Part? {
//        var imageprofile_body: MultipartBody.Part? = null
//        imageprofile_body = if (path.isNullOrEmpty()) {
//            MultipartBody.Part.createFormData(
//                key,
//                "",
//                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
//            )
//        } else {
//            val file = File(path)
//
//            MultipartBody.Part.createFormData(
//                key,
//                file.name,
//                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//            )
//        }
//        return imageprofile_body
//    }

    fun getPhoneStoragePermissions() {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        checkPermission?.getStorageAcceptedListener()
                    }

                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings
                        openSettings()
                    }
                }
            }).check()
    }


    fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        activity?.startActivityForResult(intent, 101)
    }

    open fun saveImageToSdCard(ctx: Context?, bitmapImage: Bitmap): String? {
        val root: String = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image_$n.png"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, out)
            out.flush()
            out.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

}
