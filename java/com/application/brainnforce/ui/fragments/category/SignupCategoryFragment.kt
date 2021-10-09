package com.application.brainnforce.ui.fragments.category

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.common.BNFCommon
import com.application.brainnforce.common.BNFConstants
import com.application.brainnforce.databinding.CategoryFlexListViewBinding
import com.application.brainnforce.databinding.CategoryListViewBinding
import com.application.brainnforce.databinding.SignupEditCategoryBinding
import com.application.brainnforce.model.Categories
import com.application.brainnforce.model.MainCategory
import com.application.brainnforce.model.SubCategoriesList
import com.application.brainnforce.model.User
import com.application.swapp.app.BNFBaseFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Type

@Suppress("UNREACHABLE_CODE")
class SignupCategoryFragment() : BNFBaseFragment<SignupEditCategoryBinding>() {

    private var mAdapter: RecyclerViewGenricAdapter<Categories, CategoryListViewBinding>? =
        null
    private var mFlexAdapter: RecyclerViewGenricAdapter<SubCategoriesList, CategoryFlexListViewBinding>? =
        null

    private var list: ArrayList<Categories> = ArrayList()
    private var list1: ArrayList<SubCategoriesList> = ArrayList()

    val joined: ArrayList<SubCategoriesList> = ArrayList()

    var listContact: List<SubCategoriesList>? = ArrayList()
    var stringList = ""

    private var user: User? = null

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

        callGetCategory()

        viewDataBinding.backClick.setOnClickListener {
            getContainerActivity().onBackPressed()
        }

        viewDataBinding.saveButton.setOnClickListener {
            val gson = Gson()
            var json = gson.toJson(joined)
            linkodesPref.setSaveValue(getContainerActivity(), "Category2", json)
            getContainerActivity().onBackPressed()
        }

        if (mAdapter == null) {
            mAdapter = RecyclerViewGenricAdapter(list, R.layout.category_list_view)
            { binder, model, position, itemView ->

                binder.businessId.text = model.categoryName
                setGlideString(BNFConstants.IMG_URL + model.imagePath, binder.businessImg)

                list1.clear()
                list1.addAll(model.subCategoriesList!!)

                mFlexAdapter =
                    RecyclerViewGenricAdapter(list1, R.layout.category_flex_list_view)
                    { mBinder, model1, position1, itemView1 ->

                        mBinder.accounting.text = model1.subCategoryName

                        if (listContact?.size!! > 0 || listContact?.size != null) {
                            val length = listContact?.size!!
                            for (i in 0 until length)
                                if (listContact!![i].subCategoryName!!.equals(mBinder.accounting.text.toString())) {
                                    mBinder.accounting.setBackgroundResource(R.drawable.select_flex_back)
                                    mBinder.accounting.setTextColor(Color.WHITE)
                                    joined.add(model1)
                                }
                        }
                        mBinder.accounting.setOnClickListener {
                            if (mBinder.accounting.currentTextColor == Color.BLACK) {
                                mBinder.accounting.setBackgroundResource(R.drawable.select_flex_back)
                                mBinder.accounting.setTextColor(Color.WHITE)
                                joined.add(model1)
                                mFlexAdapter!!.notifyDataSetChanged()
                            } else if (mBinder.accounting.currentTextColor == Color.WHITE) {
                                mBinder.accounting.setBackgroundResource(R.drawable.sub_caegory_flex)
                                mBinder.accounting.setTextColor(Color.BLACK)
                                joined.remove(model1)
                                mFlexAdapter?.notifyDataSetChanged()
                            }
                        }

                    }

                val layoutManager = FlexboxLayoutManager(getContainerActivity())
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                binder.recyclerViewFlex1.layoutManager = layoutManager
                binder.recyclerViewFlex1.adapter = mFlexAdapter

            }

            val mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            viewDataBinding.recyclerViewFlex.layoutManager = mLayoutManager
            viewDataBinding.recyclerViewFlex.adapter = mAdapter
        }


        if (!linkodesPref.getSaveValue(getContainerActivity(), "Category2")
                .isBlank()
        ) {
            stringList =
                linkodesPref.getSaveValue(getContainerActivity(), "Category2")
            val type: Type = object : TypeToken<List<SubCategoriesList?>?>() {}.type
            listContact = Gson().fromJson(stringList, type)

            Log.e("SIZE", listContact?.size.toString())


        }


    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.signup_edit_category
    }

    private fun callGetCategory() {
        showLoading()

        disposable.add(

            apiService.apiCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MainCategory>() {
                    override fun onSuccess(model: MainCategory) {
                        hideLoading()
                        showToast("Success Get All Category")
                        list.addAll(model.data!!.categories!!)

                        mAdapter!!.notifyDataSetChanged()
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
