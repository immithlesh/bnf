package com.application.brainnforce.ui.fragments.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.common.BNFConstants
import com.application.brainnforce.databinding.*
import com.application.brainnforce.model.*
import com.application.brainnforce.ui.fragments.auth.ChooseAccountTypeFragment
import com.application.brainnforce.ui.fragments.auth.LoginFragment
import com.application.swapp.app.BNFBaseFragment
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ProfileInformationFragment() : BNFBaseFragment<InformationTabBinding>() {

    private var mFlexAdapter: RecyclerViewGenricAdapter<UserCategories, CategoryFlexListViewBinding>? =
        null
    private var list: ArrayList<UserCategories> = ArrayList()
    private var user: User? = null

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
//                val personEmail = acct?.email
//                val personId = acct?.id
//                val personPhoto: Uri? = acct?.photoUrl

            viewDataBinding.profileFullname.setText(personName)
//                viewDataBinding.profileFullname.setText(personName)
//                viewDataBinding.profileEmail.setText(personEmail)
//                Glide.with(this).load(acct!!.photoUrl).into(viewDataBinding.imgBackProfile)
//                Glide.with(this).load(acct!!.photoUrl).into(viewDataBinding.circularImageView)


        } else {
            if (user == null) {
                callGetUserInfo()
                user = linkodesPref.retrieveUserInfo(getContainerActivity())!!
                if (linkodesPref.getSaveValue(getContainerActivity(), "Account_Type") == "Buyer") {
                    viewDataBinding.SellerLogoutId.visibility = View.INVISIBLE
                } else if (linkodesPref.getSaveValue(
                        getContainerActivity(),
                        "Account_Type"
                    ) == "Seller"
                ) {
                    viewDataBinding.SellerLogoutId.visibility = View.VISIBLE
                }

                viewDataBinding.SellerLogoutId.setOnClickListener {
                    FirebaseAuth.getInstance().signOut()
                    googleSignInClient.signOut()
                    linkodesPref.setUserLoggedInStatus(getContainerActivity(), false)
                    linkodesPref.clearPrefs(getContainerActivity())
                    displayIt(
                        ChooseAccountTypeFragment(1),
                        ChooseAccountTypeFragment::class.java.canonicalName,
                        false
                    )
                }

                viewDataBinding.profileFullname.setText(user!!.firstname + " " + user!!.lastname)
                viewDataBinding.profileLocation.setText(user!!.location)
                viewDataBinding.profileLanguage.setText(user!!.languages)


                if (mFlexAdapter == null) {
                    mFlexAdapter = RecyclerViewGenricAdapter(list, R.layout.category_flex_list_view)
                    { binder, model, position, itemView ->

                        binder.accounting.text = model.subCategory!!.subCategoryName

                    }

                    val layoutManager = FlexboxLayoutManager(getContainerActivity())
                    layoutManager.flexDirection = FlexDirection.ROW
                    layoutManager.justifyContent = JustifyContent.FLEX_START
                    viewDataBinding.recyclerViewFlex.layoutManager = layoutManager
                    viewDataBinding.recyclerViewFlex.adapter = mFlexAdapter

                }


            }


//            callGetUserInfo()
        }

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.information_tab
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
                        list.clear()
                        list.addAll(model.data!!.user!!.userCategoriesList!!)
                        mFlexAdapter!!.notifyDataSetChanged()

                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.d("logE", "" + e)
                    }
                })
        )
    }

}

