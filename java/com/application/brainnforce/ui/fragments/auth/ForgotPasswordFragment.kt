package com.application.brainnforce.ui.fragments.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.common.BNFCommon
import com.application.brainnforce.databinding.FragForgotPasswordBinding
import com.application.brainnforce.utils.content
import com.application.swapp.app.BNFBaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class ForgotPasswordFragment : BNFBaseFragment<FragForgotPasswordBinding>() {

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

        viewDataBinding.email.setFilters(arrayOf(EmojiExcludeFilter()))

        viewDataBinding.submitClick.setOnClickListener {
            if (validateEditText(viewDataBinding.email) &&
                isEmailValid(viewDataBinding.email)
            ) {
                callForgotPassword()
            }
        }

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_forgot_password
    }


    private fun callForgotPassword() {
        showLoading()

        val jsonObject = JSONObject()
        jsonObject.put("email", viewDataBinding.email.content)

        val requestBody =
            jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        disposable.add(

            apiService.apiSendOtp(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Any>() {

                    override fun onSuccess(model: Any) {
                        hideLoading()
                        showToast("We will send the password recovery instructions on your registered email address")
                        displayIt(
                            EnterOtpFragment(viewDataBinding.email.content),
                            EnterOtpFragment::class.java.canonicalName,
                            false
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
