package com.application.brainnforce.ui.fragments.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.databinding.*
import com.application.brainnforce.model.UserDetail
import com.application.brainnforce.ui.fragments.auth.ChooseAccountTypeFragment
import com.application.brainnforce.ui.fragments.auth.LoginFragment
import com.application.swapp.app.BNFBaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class ProfileServicesFragment() :
    BNFBaseFragment<ServicesTabBinding>() {
    private var list: ArrayList<UserDetail> = ArrayList()

    private var mServiceAdapter: RecyclerViewGenricAdapter<UserDetail, ServiceItemListBinding>? =
        null


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

        if (linkodesPref.getSaveValue(getContainerActivity(), "Account_Type") == "Buyer") {
            viewDataBinding.SellerLogoutId.visibility = View.INVISIBLE
        }else if (linkodesPref.getSaveValue(getContainerActivity(), "Account_Type") == "Seller") {
            viewDataBinding.SellerLogoutId.visibility = View.VISIBLE
        }

        list = ArrayList()
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())


        mServiceAdapter = RecyclerViewGenricAdapter(list, R.layout.service_item_list)
        { mbinder, model, position, itemView ->

            mbinder.imageProfile.visibility = View.GONE
            mbinder.rateText.visibility = View.GONE
        }


        val mLayoutManagers = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        viewDataBinding.serviceRecyclerView.layoutManager = mLayoutManagers
        viewDataBinding.serviceRecyclerView.adapter = mServiceAdapter

        viewDataBinding.SellerLogoutId.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            googleSignInClient.signOut()
            linkodesPref.setUserLoggedInStatus(getContainerActivity(), false)
            linkodesPref.clearPrefs(getContainerActivity())
            displayIt(ChooseAccountTypeFragment(1), ChooseAccountTypeFragment::class.java.canonicalName, false)
        }

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.services_tab
    }

}

