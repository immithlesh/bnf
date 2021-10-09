package com.application.brainnforce.ui.fragments.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.databinding.*
import com.application.brainnforce.model.LoginCrediental
import com.application.brainnforce.model.LoginRequestModel
import com.application.brainnforce.model.UserDetail
import com.application.brainnforce.model.changePassword
import com.application.brainnforce.ui.fragments.auth.LoginFragment
import com.application.brainnforce.ui.fragments.home.HomeFragment
import com.application.brainnforce.utils.content
import com.application.swapp.app.BNFBaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ChangePasswordFragment : BNFBaseFragment<FragChangePswdBinding>() {

    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun isBottomBarVisible(): Boolean {
        return false
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.logoIcon.setOnClickListener {
            getContainerActivity().onBackPressed()
        }

        viewDataBinding.resetClick.setOnClickListener {
            if (validatePasswordEditText(viewDataBinding.currentPassword) &&
                validatePasswordEditText(viewDataBinding.newPassword) &&
                comparePassword(viewDataBinding.newPassword, viewDataBinding.cnfPassword)
            ) {
                callChangePassowrd()
            }

        }

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_change_pswd
    }

    private fun callChangePassowrd() {
        showLoading()
        val request = changePassword().apply {
            currentPassword = viewDataBinding.currentPassword.content
            newPassword = viewDataBinding.newPassword.content
        }

        disposable.add(
            apiService.apiChangePassword(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Any>() {

                    override fun onSuccess(model: Any) {
                        hideLoading()
                        showToast("Password Successfully Changed")
                        getContainerActivity().onBackPressed()
//                        displayIt(LoginFragment(), LoginFragment::class.java.canonicalName, false)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.d("logE", "" + e)
                    }
                })
        )
    }

}
