package com.application.brainnforce.ui.fragments.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.common.BNFCommon
import com.application.brainnforce.common.PasswordTransformationMethod
import com.application.brainnforce.databinding.FragCreateNewPasswordBinding
import com.application.brainnforce.utils.content
import com.application.swapp.app.BNFBaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class CreateNewPasswordFragment(var email: String) :
    BNFBaseFragment<FragCreateNewPasswordBinding>() {

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


        viewDataBinding.newPassword.transformationMethod = PasswordTransformationMethod()
        viewDataBinding.cnfPassword.transformationMethod = PasswordTransformationMethod()

        viewDataBinding.common.backClick.setOnClickListener {
            getContainerActivity().onBackPressed()
        }

        viewDataBinding.common.titleTxt.text =
            getContainerActivity().getString(R.string.create_new_pass)
        viewDataBinding.common.subTitleTxt.text =
            getContainerActivity().getString(R.string.create_your_new_pass)


        viewDataBinding.resetClick.setOnClickListener {
            if (validatePasswordEditText(viewDataBinding.newPassword) &&
                comparePassword(viewDataBinding.newPassword, viewDataBinding.cnfPassword)
            ) {
                callNewPassword()
            }
        }

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_create_new_password
    }


    private fun callNewPassword() {
        showLoading()

        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("newPassword", viewDataBinding.newPassword.content)

        val requestBody =
            jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        disposable.add(

            apiService.apiResetPassword(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Any>() {

                    override fun onSuccess(model: Any) {
                        hideLoading()
                        showToast("Password Successfully Changed")
                        displayIt(
                            LoginFragment(),
                            LoginFragment::class.java.canonicalName, false
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