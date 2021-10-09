package com.application.brainnforce.common

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.text.InputFilter
import android.text.InputType
import android.text.format.DateUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.application.brainnforce.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class BNFCommon {

    companion object {

        /* Show warning snackbar if user on home fragment and click on back button. */
        fun showExitWarning(layout: ViewGroup, activity: AppCompatActivity) {
            val snack =
                Snackbar.make(layout, "To exit, Tap back button again.", Snackbar.LENGTH_SHORT)
            snack.setActionTextColor(ContextCompat.getColor(activity, R.color.white))
            snack.setAction("EXIT NOW") {
                activity.finish()
            }
            val snackBarView = snack.view
            snackBarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.purple))
            snack.show()
        }

//        /* Validate EditText that checks empty. */
//        fun validateEditText(et: EditText): Boolean {
//
//            return if (et.text.toString() == "") {
//                et.requestFocus()
//                et.error = "This field can't be empty"
//                false
//            } else
//                true
//        }

//        fun validatePhoneNumber(et: EditText): Boolean {
//
//            return if (et.text.toString().length < 12) {
//                et.requestFocus()
//                et.error = "Entered phone number is not valid, please try again"
//                false
//            } else
//                true
//        }
//
//        fun validateTextView(mActivity: AppCompatActivity, et: TextView, errorMsg: String): Boolean {
//
//            return if (et.text.toString() == "") {
//                setResponseDialog(mActivity, errorMsg)
//                false
//            } else
//                true
//        }


        //        /* Validate if two EditText are same. */
//        fun validateBothTextNotSame(et1: EditText, et2: String): Boolean {
//
//            var input = et1.text.toString()
//            input = input.replace(" ", "")
//
//            if (input == et2) {
//                et1.requestFocus()
//                et1.error = "Your old and new number are the same"
//                return false
//            } else
//                return true
//        }

        fun dpToPx(dp: Int, activity: AppCompatActivity): Int {
            val r = activity.resources
            return Math.round(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp.toFloat(),
                    r.displayMetrics
                )
            )
        }


//        /* Validate EditText that minimum 3 characters. */
//        fun minimumCharactersEditText(et: EditText): Boolean {
//
//            if (et.text.toString().length < 3) {
//                et.requestFocus()
//                et.error = "Name must be contains minimum 3 characters."
//                return false
//            } else
//                return true
//        }
//
//        /* Checks entered email is valid or not. */
//        fun isEmailValid(et: EditText): Boolean {
//            val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(et.text.toString()).matches()
//
//            return if (isValid) {
//                true
//            } else {
//                et.requestFocus()
//                et.error = "Entered email address is not valid"
//                false
//            }
//        }

        //        /* Show/Hide password view from eye click */
        fun showHidePassword(mActivity: AppCompatActivity, view: View, et: EditText) {
            if (view.isSelected) {
                view.isSelected = false
                et.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                view.isSelected = true
                et.inputType = InputType.TYPE_CLASS_TEXT
            }
            et.setSelection(et.text.length)
            et.typeface = Typeface.createFromAsset(mActivity.assets, "rodger_light.ttf")
        }

        /* Verify that entered password meets the requirement. */
        private fun isValidPassword(password: String): Boolean {

            val pattern: Pattern
            val matcher: Matcher

            val passwordPattern = "^(?=.*[0-9])(?=.*[@#\$%^*&+=!_-])(?=.*[a-zA-Z])(?=\\S+$).{8,}$"
            // (?=.*[@#$%^&+=]) // special character
            // (?=.*[A-Z])      // capital letter
            // (?=.*[0-9])      // digit
            // (?=.*[a-z])      // small letter

            pattern = Pattern.compile(passwordPattern)
            matcher = pattern.matcher(password)

            return matcher.matches()
        }

        fun validatePasswordET(et: EditText): Boolean {
            if (isValidPassword(et.text.toString())) {
                return true
            } else {
                et.requestFocus()
                et.error =
                    "Password must be eight characters long and contain at least one alphabet, one number and one special character"
                return false
            }
        }

        fun formatFileSize(size: Long): String? {
            var hrSize: String? = null
            val b = size.toDouble()
            val k = size / 1024.0
            val m = size / 1024.0 / 1024.0
            val g = size / 1024.0 / 1024.0 / 1024.0
            val t = size / 1024.0 / 1024.0 / 1024.0 / 1024.0
            val dec1 = DecimalFormat("0.00")
            val dec2 = DecimalFormat("0")
            hrSize = if (t > 1) {
                if (isDouble(t)) dec1.format(t) + " TB" else dec2.format(t) + " TB"
            } else if (g > 1) {
                if (isDouble(g)) dec1.format(g) + " GB" else dec2.format(g) + " GB"
            } else if (m > 1) {
                if (isDouble(m)) dec1.format(m) + " MB" else dec2.format(m) + " MB"
            } else if (k > 1) {
                if (isDouble(k)) dec1.format(k) + " KB" else dec2.format(k) + " KB"
            } else {
                if (isDouble(b)) dec1.format(b) + " B" else dec2.format(b) + " B"
            }
            return hrSize
        }

        private fun isDouble(value: Double): Boolean {
            return value % 1 == 0.0
        }


//
//
//        fun comparePassword(etOne: EditText, etTwo: EditText): Boolean {
//
//            if (etOne.text.toString() == etTwo.text.toString()) {
//                return true
//            } else {
//                etTwo.requestFocus()
//                etTwo.error = "Your confirm password doesn't match."
//                return false
//            }
//        }
//
//        fun convertMeterToKM(distanceInMeters: Double): String {
//            return "${(distanceInMeters / 1000).toInt()} km"
//        }

        fun showError(e: Throwable, activity: AppCompatActivity, listener: OnError) {

            var isNetworkException: Boolean

            try {
                val obj = JSONObject((e as HttpException).response().errorBody()!!.string())
                val error = obj.optString("error")
                val code = e.code()

                isNetworkException = false

                L.e("Error Message", "$error $code")

                setResponseDialog(activity, error, listener)


            } catch (e: Exception) {
                isNetworkException = true
                e.printStackTrace()
            }

            L.e("TAG", "onError: " + e.message)
            if (isNetworkException)
                setResponseDialog(activity, e.message!!)
        }


        fun showError(e: Throwable, activity: AppCompatActivity) {

            var isNetworkException: Boolean

            try {
                val obj = JSONObject((e as HttpException).response().errorBody()!!.string())
                val error = obj.optString("message")
                val code = e.code()

                isNetworkException = false

                L.e("Error Message", "$error $code")

//                setResponseDialog(activity, error)

                Toast.makeText(activity, error, Toast.LENGTH_LONG).show()


            } catch (e: Exception) {
                isNetworkException = true
                e.printStackTrace()
            }

            L.e("TAG", "onError: " + e.message)
//            if (isNetworkException) {
//
//                if (e.message!!.contains("Failed to connect"))
//                    setResponseDialog(activity, activity.getString(R.string.internet_error))
//                else
//                    setResponseDialog(activity, e.message!!)
//
//            }
        }


        fun setLoadingDialog(activity: AppCompatActivity): Dialog {

//            val dialog = Dialog(activity,android.R.style.Theme_Light_NoTitleBar)
//            val window = dialog.window
//
//            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            window.setBackgroundDrawableResource(R.color.colorWhiteTransparent)
//            dialog.show()

            val dialog = Dialog(activity)
            dialog.show()
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#B3ffffff")))
            }
            dialog.setContentView(R.layout.loader)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            return dialog
        }

        fun setResponseDialog(activity: AppCompatActivity, message: String) {
            val dialog = BottomSheetDialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
//            dialog.setContentView(R.layout.dialog_bottom_response_layout)

//            val ok = dialog.findViewById<Button>(R.id.okClick)
            val msg = dialog.findViewById<TextView>(R.id.message)

            msg!!.text = message

//            ok!!.setOnClickListener {
//                dialog.dismiss()
//            }

            dialog.show()
        }

        fun setResponseDialog(activity: AppCompatActivity, message: String, listener: OnError) {
            val dialog = BottomSheetDialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
//            dialog.setContentView(R.layout.dialog_bottom_response_layout)

//            val ok = dialog.findViewById<Button>(R.id.okClick)
            val msg = dialog.findViewById<TextView>(R.id.message)

            msg!!.text = message

//            ok!!.setOnClickListener {
//                dialog.dismiss()
//                listener.onOkClick()
//            }

            dialog.show()
        }

        fun disableWhiteSpace(editText: EditText) {
            val filters = arrayOfNulls<InputFilter>(2)
            filters[0] = InputFilter { source, start, end, _, _, _ ->
                if (end > start) {

                    val acceptedChars = charArrayOf(
                        'a',
                        'b',
                        'c',
                        'd',
                        'e',
                        'f',
                        'g',
                        'h',
                        'i',
                        'j',
                        'k',
                        'l',
                        'm',
                        'n',
                        'o',
                        'p',
                        'q',
                        'r',
                        's',
                        't',
                        'u',
                        'v',
                        'w',
                        'x',
                        'y',
                        'z',
                        'A',
                        'B',
                        'C',
                        'D',
                        'E',
                        'F',
                        'G',
                        'H',
                        'I',
                        'J',
                        'K',
                        'L',
                        'M',
                        'N',
                        'O',
                        'P',
                        'Q',
                        'R',
                        'S',
                        'T',
                        'U',
                        'V',
                        'W',
                        'X',
                        'Y',
                        'Z',
                        '0',
                        '1',
                        '2',
                        '3',
                        '4',
                        '5',
                        '6',
                        '7',
                        '8',
                        '9',
                        '@',
                        '.',
                        '_',
                        '#',
                        '$',
                        '%',
                        '&',
                        '*',
                        '-',
                        '+',
                        '(',
                        ')',
                        '!',
                        '"',
                        '\'',
                        ':',
                        ';',
                        '/',
                        '?',
                        ',',
                        '~',
                        '`',
                        '|',
                        '\\',
                        '^',
                        '<',
                        '>',
                        '{',
                        '}',
                        '[',
                        ']',
                        '=',
                        '£',
                        '¥',
                        '€',
                        '.',
                        '¢',
                        '•',
                        '©'
                    )

                    for (index in start until end) {
                        if (!String(acceptedChars).contains(source[index].toString())) {
                            return@InputFilter ""
                        }
                    }
                }
                null
            }

            filters[1] = InputFilter.LengthFilter(25)

            editText.filters = filters
        }


        private fun showSettingsDialog(mActivity: AppCompatActivity) {
            AlertDialog.Builder(mActivity).apply {
                setTitle("Need Permissions")
                setCancelable(false)
                setMessage("This app needs permission to use this feature. You can grant them in app settings.")
                setPositiveButton("GOTO SETTINGS") { dialog, _ ->
                    dialog.dismiss()
                    openSettings(mActivity)
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }

        private fun openSettings(activity: AppCompatActivity) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivityForResult(intent, 1010)
        }


        @Throws(ParseException::class)
        fun setDateInFormat(dateInString: String, outputFormat: String): String {

            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)
            val outputFormatDate = SimpleDateFormat(outputFormat, Locale.ENGLISH)

            format.timeZone = TimeZone.getTimeZone("UTC")

            val result = format.parse(dateInString)

            return outputFormatDate.format(result!!)
        }

        @Throws(ParseException::class)
        fun setDOBInFormat(
            dateInString: String,
            inputFormat: String,
            outputFormat: String
        ): String {

            val format = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            val outputFormatDate = SimpleDateFormat(outputFormat, Locale.ENGLISH)

            format.timeZone = TimeZone.getTimeZone("UTC")

            val result = format.parse(dateInString)

            return outputFormatDate.format(result!!)
        }

        @Throws(ParseException::class)
        fun getDateInMillies(dateInString: String): Long {
            val format = SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH)
//            format.timeZone = TimeZone.getTimeZone("UTC")
            val result = format.parse(dateInString)
            return result!!.time
        }


        fun getTimeAgo(dateInString: String, tv: TextView) {

//            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)
            format.timeZone = TimeZone.getTimeZone("UTC")
            val date = format.parse(dateInString)
            tv.text = DateUtils.getRelativeTimeSpanString(
                date!!.time,
                Calendar.getInstance().timeInMillis,
                DateUtils.MINUTE_IN_MILLIS
            )

        }


    }


    interface OnError {

        fun onOkClick()
    }

}