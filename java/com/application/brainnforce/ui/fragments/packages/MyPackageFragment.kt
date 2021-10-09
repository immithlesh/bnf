package com.application.brainnforce.ui.fragments.packages

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.databinding.FragMyPackageBinding
import com.application.brainnforce.databinding.MyPackageList1Binding
import com.application.brainnforce.databinding.MyPackageListBinding
import com.application.brainnforce.model.LoginCrediental
import com.application.brainnforce.model.UserPackages
import com.application.brainnforce.model.UserParentPackage
import com.application.swapp.app.BNFBaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

class MyPackageFragment : BNFBaseFragment<FragMyPackageBinding>() {

    private var mAdapter: RecyclerViewGenricAdapter<UserParentPackage, MyPackageListBinding>? =
        null
    private var mFlexAdapter: RecyclerViewGenricAdapter<UserPackages, MyPackageList1Binding>? =
        null

    private var list: ArrayList<UserParentPackage> = ArrayList()
    private var list1: ArrayList<UserPackages> = ArrayList()

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

        callGetUserInfo()

        viewDataBinding.backIcon.setOnClickListener {
            getContainerActivity().onBackPressed()
        }

        viewDataBinding.btnCreate.setOnClickListener {

            linkodesPref.clear(getContainerActivity(), "Category2")
            displayIt(
                CreatePackageFragment(0, null, ""),
                CreatePackageFragment::class.java.canonicalName,
                true
            )
        }

        if (mAdapter == null) {
            mAdapter = RecyclerViewGenricAdapter(list, R.layout.my_package_list)
            { binder, model, position, itemView ->

                if (model.title != null) {
                    binder.generalText.text = model.title
                }

                binder.addPackageBtn.setOnClickListener {
                    displayIt(
                        CreatePackageFragment(0, model.id, model.title),
                        CreatePackageFragment::class.java.canonicalName,
                        true
                    )
                }

                list1.clear()
                list1.addAll(model.userPackagesList!!)

                mFlexAdapter = RecyclerViewGenricAdapter(list1, R.layout.my_package_list1)
                { mBinder, model1, position1, itemView1 ->

                    if (list1.size > 1) {
                        mBinder.view.visibility = View.VISIBLE
                    } else {
                        mBinder.view.visibility = View.GONE
                    }

                    if (model1.duration != null) {
                        mBinder.durationText.text = model1.duration.toString()
                    }

                    if (model1.price != null) {
                        mBinder.priceText.text = model1.price.toString()
                    }

                    mBinder.packageDelete.setOnClickListener {
                        callPackageDelete(model1.id)
                    }

                    mBinder.packageArrow.setOnClickListener {

                        linkodesPref.storePackageInfo(getContainerActivity(), model1)
                        displayIt(
                            CreatePackageFragment(1, model.id, model.title),
                            CreatePackageFragment::class.java.canonicalName,
                            true
                        )
                    }

                }

                val mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                binder.recyclerViewList.layoutManager = mLayoutManager
                binder.recyclerViewList.adapter = mFlexAdapter

            }

            val mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            viewDataBinding.recyclerView.layoutManager = mLayoutManager
            viewDataBinding.recyclerView.adapter = mAdapter
        }

    }

    private fun callPackageDelete(id: Int?) {
        showLoading()
        list1.clear()
        disposable.add(
            apiService.apiDeletePackage(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Any>() {
                    override fun onSuccess(model: Any) {
                        hideLoading()
                        list1.clear()
                        list.clear()
                        callGetUserInfo()
                        mAdapter!!.notifyDataSetChanged()
                        mFlexAdapter!!.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.d("logE", "" + e)
                    }
                })
        )
    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_my_package
    }

    private fun callGetUserInfo() {
        showLoading()
        list.clear()
        disposable.add(
            apiService.apiGetUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginCrediental>() {
                    override fun onSuccess(model: LoginCrediental) {
                        hideLoading()
//                        showToast("Success Get All Category")
                        if (model.data!!.user!!.userParentPackageList!!.size == 0) {
                            viewDataBinding.emptyText.visibility = View.VISIBLE
                            viewDataBinding.recyclerView.visibility = View.GONE
                        } else {
                            viewDataBinding.emptyText.visibility = View.GONE
                            viewDataBinding.recyclerView.visibility = View.VISIBLE
                        }
                        list.addAll(model.data!!.user!!.userParentPackageList!!)
                        mAdapter!!.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.d("logE", "" + e)
                    }
                })
        )
    }

}
