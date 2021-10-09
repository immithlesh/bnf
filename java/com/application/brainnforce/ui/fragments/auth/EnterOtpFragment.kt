package com.application.brainnforce.ui.fragments.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.common.BNFCommon
import com.application.brainnforce.databinding.FragEnterOtpBinding
import com.application.swapp.app.BNFBaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class EnterOtpFragment(var email: String) : BNFBaseFragment<FragEnterOtpBinding>() {

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

        viewDataBinding.common.backClick.setOnClickListener {
            getContainerActivity().onBackPressed()
        }

        viewDataBinding.common.titleTxt.text = getContainerActivity().getString(R.string.enter_otp)
        viewDataBinding.common.subTitleTxt.text =
            getContainerActivity().getString(R.string.enter_otp_code)

        viewDataBinding.verifyClick.setOnClickListener {
            if (validateEditText(viewDataBinding.pinview)) {
                callEnterOTP()
            }
//            callEnterOTP()
        }

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_enter_otp
    }


    private fun callEnterOTP() {
        showLoading()

        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("otp", viewDataBinding.pinview.text.toString())

        val requestBody =
            jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        disposable.add(

            apiService.apiVerifyOtp(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Any>() {

                    override fun onSuccess(model: Any) {
                        hideLoading()
                        showToast("OTP verfied successfully")
                        displayIt(
                            CreateNewPasswordFragment(email),
                            CreateNewPasswordFragment::class.java.canonicalName, false
                        )

                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        BNFCommon.showError(e, getContainerActivity())
                        Log.d("logE", "" + e)
                    }
                })
        )
    }

}