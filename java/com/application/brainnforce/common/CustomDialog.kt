package com.application.brainnforce.common

import android.app.Dialog
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.application.brainnforce.R
import com.application.brainnforce.app.BrainNForceActivity

class CustomDialog(val mContainer: BrainNForceActivity) {

    var dialog: Dialog? = null

    private fun initDialog(layout: View): Dialog {
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
        dialog = CustomDialog(mContainer, R.style.mytheme)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(layout)
        return dialog!!
    }

    class CustomDialog(context: Context, themeId: Int) : Dialog(context, themeId) {
        override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
            currentFocus?.let {
                val imm: InputMethodManager = context.applicationContext.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as (InputMethodManager)
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
    }

}