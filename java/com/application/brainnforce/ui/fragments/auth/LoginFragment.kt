package com.application.brainnforce.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.application.brainnforce.BrainNForceApplication.Companion.RC_SIGN_IN
import com.application.brainnforce.BrainNForceApplication.Companion.TAG
import com.application.brainnforce.R
import com.application.brainnforce.common.BNFCommon
import com.application.brainnforce.common.PasswordTransformationMethod
import com.application.brainnforce.databinding.FragLoginBinding
import com.application.brainnforce.local.PrefUtils
import com.application.brainnforce.model.LoginCrediental
import com.application.brainnforce.model.LoginRequestModel
import com.application.brainnforce.ui.fragments.home.DashboardFragment
import com.application.brainnforce.ui.fragments.home.HomeFragment
import com.application.brainnforce.utils.ConstantStr
import com.application.brainnforce.utils.content
import com.application.swapp.app.BNFBaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

private const val ARG_PARAM1 = "param1"

class LoginFragment : BNFBaseFragment<FragLoginBinding>() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(getContainerActivity(), gso)
        auth = FirebaseAuth.getInstance()

        getContainerActivity().viewDataBinding.bottomBar.visibility = View.GONE

        viewDataBinding.password.transformationMethod = PasswordTransformationMethod()

        viewDataBinding.forgotClick.setOnClickListener {
            displayIt(
                ForgotPasswordFragment(),
                ForgotPasswordFragment::class.java.canonicalName,
                true
            )
        }
        viewDataBinding.social.googleID.setOnClickListener {
            val currentUser = auth.currentUser
            updateUI(currentUser)
            googleSignInClient.signOut()
            GoogleSignIn()
            val name = currentUser?.displayName
            val email = currentUser?.email
            val profilePhoto = currentUser?.photoUrl
            Log.e("name:", name.toString())
            Log.e("name:", email.toString())
            Log.e("name:", profilePhoto.toString())
        }

        viewDataBinding.signUpBox.setOnClickListener {
            displayIt(
                CreateAccountFragment(),
                CreateAccountFragment::class.java.canonicalName,
                true
            )
        }

        viewDataBinding.backBtnLogin.setOnClickListener {
            displayIt(
                ChooseAccountTypeFragment(1),
                ChooseAccountTypeFragment::class.java.canonicalName,
                false
            )
            linkodesPref.clear(getContainerActivity(), "Account_Type")
        }

        viewDataBinding.email.setFilters(arrayOf(EmojiExcludeFilter()))

        viewDataBinding.btnLogin.setOnClickListener {

            if (validateEditText(viewDataBinding.email) &&
                validateEditText(viewDataBinding.password) &&
                isEmailValid(viewDataBinding.email)
            ) {
                if (linkodesPref.getSaveValue(getContainerActivity(), "Account_Type") == "Buyer") {
                    buyerLogin()
                } else {
                    sellerLogin()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
                socialLoginApi(currentUser = auth.currentUser)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(getContainerActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }

    private fun GoogleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(user: FirebaseUser?) {}
    private fun buyerLogin() {
        showLoading()
        val request = LoginRequestModel().apply {
            email = viewDataBinding.email.content
            password = viewDataBinding.password.content
            roleId = "3"
        }

        disposable.add(
            apiService.apiLogin(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginCrediental>() {

                    override fun onSuccess(model: LoginCrediental) {
                        hideLoading()

                        linkodesPref.storeAuthKey(
                            getContainerActivity(),
                            model.data!!.accessToken!!
                        )
                        linkodesPref.setUserLoggedInStatus(getContainerActivity(), true)
                        linkodesPref.storeUserInfo(getContainerActivity(), model.data!!.user!!)

                        displayIt(HomeFragment(), HomeFragment::class.java.canonicalName, false)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        BNFCommon.showError(e, getContainerActivity())
                        Log.d("logE", "" + e)
                    }
                })
        )
    }

    private fun sellerLogin() {

        showLoading()
        val request = LoginRequestModel().apply {
            email = viewDataBinding.email.content
            password = viewDataBinding.password.content
            roleId = "2"
        }

        disposable.add(

            apiService.apiLogin(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginCrediental>() {

                    override fun onSuccess(model: LoginCrediental) {
                        hideLoading()

                        linkodesPref.storeAuthKey(
                            getContainerActivity(),
                            model.data!!.accessToken!!
                        )
                        linkodesPref.setUserLoggedInStatus(getContainerActivity(), true)
                        linkodesPref.storeUserInfo(getContainerActivity(), model.data!!.user!!)

                        displayIt(
                            DashboardFragment(),
                            DashboardFragment::class.java.canonicalName,
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

    private fun socialLoginApi(currentUser: FirebaseUser?) {

        showLoading()
        val jsonObj = JsonObject()
        jsonObj.addProperty("role_id", if (accountType==ConstantStr.SELER)"2" else "3")
        jsonObj.addProperty("socialMediaType", "google")
        jsonObj.addProperty("socialMediaId", currentUser?.uid)

        disposable.add(
            apiService.socialLoginApi(jsonObj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginCrediental>() {
                    override fun onSuccess(model: LoginCrediental) {
                        hideLoading()
                        linkodesPref.storeAuthKey(
                            getContainerActivity(),
                            model.data?.accessToken!!
                        )
                        PrefUtils().setUserLoggedInStatus(getContainerActivity(), true)
                        PrefUtils().storeUserInfo(getContainerActivity(), model.data!!.user!!)
                        when (accountType) {
                            ConstantStr.BUYER -> displayIt(
                                HomeFragment(),
                                HomeFragment::class.java.canonicalName,
                                false
                            )
                            ConstantStr.SELER -> displayIt(
                                DashboardFragment(),
                                DashboardFragment::class.java.canonicalName,
                                false
                            )
                        }

                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        BNFCommon.showError(e, getContainerActivity())
                        Log.d("logE", "" + e)
                    }
                })
        )

    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_login
    }

    private var accountType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            accountType = it.getString(ARG_PARAM1)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}
