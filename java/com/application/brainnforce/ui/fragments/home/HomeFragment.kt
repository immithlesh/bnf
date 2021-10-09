package com.application.brainnforce.ui.fragments.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.brainnforce.R
import com.application.brainnforce.base.adapter.RecyclerViewGenricAdapter
import com.application.brainnforce.common.BNFConstants
import com.application.brainnforce.databinding.*
import com.application.brainnforce.model.*
import com.application.brainnforce.ui.fragments.notification.FavoritesFragment
import com.application.brainnforce.ui.fragments.profile.SellerProfileFragment
import com.application.swapp.app.BNFBaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class HomeFragment : BNFBaseFragment<FragHomeBinding>() {

    private var mCategoryAdapter: RecyclerViewGenricAdapter<UserDetail, CategoryItemListBinding>? =
        null
    private var list: ArrayList<UserDetail> = ArrayList()

    private var listUser: ArrayList<Seller> = ArrayList()

    private var mSellerAdapter: RecyclerViewGenricAdapter<Seller, ServiceItemListBinding>? =
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

        callGetSellerList()

        getContainerActivity().viewDataBinding.bottomNavigationView.menu.getItem(2).isChecked =
            true
        getContainerActivity().viewDataBinding?.addPostClick?.visibility = View.VISIBLE
        getContainerActivity().viewDataBinding?.addPostClick2?.visibility = View.INVISIBLE

        getContainerActivity().BuyerSide()

        if (mSellerAdapter == null) {
            mSellerAdapter = RecyclerViewGenricAdapter(listUser, R.layout.service_item_list)
            { binder, model, position, itemView ->


                setGlideString(
                    BNFConstants.IMG_URL + model.profileImagePath,
                    binder.imageProfile
                )
                if (model.coverImagePath == null) {
                    setGlideString(
                        BNFConstants.IMG_URL + model.profileImagePath,
                        binder.coverImageID
                    )
                } else {
                    setGlideString(
                        BNFConstants.IMG_URL + model.coverImagePath,
                        binder.coverImageID
                    )
                }
//                binder.numText.text = model.hourlyPriceForVideoConfercing.toString()
//                binder.serviceTitle.text = model.bio.toString()
                binder.imageProfile.setOnClickListener {
                    displayIt(
                        SellerProfileFragment(),
                        SellerProfileFragment::class.java.canonicalName,
                        true
                    )
                }

                binder.mainLayout.setOnClickListener {
                    displayIt(
                        ServiceDetailListFragment(),
                        ServiceDetailListFragment::class.java.canonicalName,
                        true
                    )
                }


            }

            val mLayoutManagers = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
            viewDataBinding.serviceRecyclerView.layoutManager = mLayoutManagers
            viewDataBinding.serviceRecyclerView.adapter = mSellerAdapter
            mSellerAdapter?.notifyDataSetChanged()

        }



        viewDataBinding.favouriteIcon.setOnClickListener {
            displayIt(FavoritesFragment(), FavoritesFragment::class.java.canonicalName, true)
        }

        viewDataBinding.logoIcon.setOnClickListener {
            displayIt(HomeFragment(), HomeFragment::class.java.canonicalName, false)

        }

        viewDataBinding.filterIcon.setOnClickListener {
            callFilterDialog(object : OnDialogListener {

                override fun onYesClick() {
                }

            })
        }

        list = ArrayList()
        list.add(UserDetail())
        list.add(UserDetail())
        list.add(UserDetail())


        val array_image = ArrayList<ArrayImage>()
        array_image.add(ArrayImage(R.drawable.select_mathematics, true, R.drawable.unselect_mathmatics, "MATHEMATICS"))
        array_image.add(
            ArrayImage(
                R.drawable.select_language,
                false,
                R.drawable.unselect_language,
                "LANGUAGES"
            )
        )
        array_image.add(ArrayImage(R.drawable.select_physcial_science, false, R.drawable.unselect_physcial_science, "PHYSICAL SCIENCES"))


        mCategoryAdapter = RecyclerViewGenricAdapter(list, R.layout.category_item_list)
        { binder, model, position, itemView ->

            binder.icon.setOnClickListener {

                if (position == 0) {
                    val listOfNames = ArrayList<Marketplace>()
                    val listOfNames2 = ArrayList<Marketplace>()
                    listOfNames.add(Marketplace(false, "All Subcategories"))
                    listOfNames.add(Marketplace(true, "Arithmetic"))
                    listOfNames.add(Marketplace(false, "Counting"))
                    listOfNames.add(Marketplace(false, "Geometry"))
                    listOfNames.add(Marketplace(false, "Algebra"))
                    listOfNames.add(Marketplace(false, "Trigonometry"))

                    listOfNames2.add(Marketplace(false, "Statistics & Probability"))
                    listOfNames2.add(Marketplace(false, "Pre-Calculus "))
                    listOfNames2.add(Marketplace(false, "Calculus"))
                    listOfNames2.add(Marketplace(false, "Linear Algebra"))
                    listOfNames2.add(Marketplace(false, "Differential Equations"))
                    listOfNames2.add(Marketplace(false, "SAT & ACT Mathematics"))
                    recursiveView(listOfNames, listOfNames2)
                }
                else if (position == 1) {
                    val listOfNames = ArrayList<Marketplace>()
                    val listOfNames2 = ArrayList<Marketplace>()
                    listOfNames.add(Marketplace(false, "All Subcategories"))
                    listOfNames.add(Marketplace(true, "English"))
                    listOfNames.add(Marketplace(false, "Spanish"))
                    listOfNames.add(Marketplace(false, "French"))
                    listOfNames.add(Marketplace(false, "German"))
                    listOfNames.add(Marketplace(false, "Russian"))
                    listOfNames.add(Marketplace(false, "Chinese - Mandarin"))

                    listOfNames2.add(Marketplace(false, "Filipino"))
                    listOfNames2.add(Marketplace(false, "Hindi"))
                    listOfNames2.add(Marketplace(false, "American Sign Language"))
                    listOfNames2.add(Marketplace(false, "Portuguese"))
                    listOfNames2.add(Marketplace(false, "Japanese"))
                    listOfNames2.add(Marketplace(false, "Arabic"))
                    recursiveView(listOfNames, listOfNames2)
                }
                else {
                    val listOfNames = ArrayList<Marketplace>()
                    val listOfNames2 = ArrayList<Marketplace>()
                    listOfNames.add(Marketplace(false, "All Subcategories"))
                    listOfNames.add(Marketplace(true, "Physics"))
                    listOfNames.add(Marketplace(false, "Chemistry"))
                    listOfNames.add(Marketplace(false, "Engineering"))

                    listOfNames2.add(Marketplace(false, "Materials Science"))
                    listOfNames2.add(Marketplace(false, "Earth Sciences"))
                    listOfNames2.add(Marketplace(false, "Astronomy"))
                    recursiveView(listOfNames, listOfNames2)
                }
                for (data in array_image) {
                    if (data.value) {
                        data.value = false
                        break
                    }
                }
                array_image.get(position).value = !array_image.get(position).value
                mCategoryAdapter?.notifyDataSetChanged()


            }


            if (array_image[position].value) {
                binder.icon.setImageDrawable(
                    ContextCompat.getDrawable(
                        getContainerActivity(),
                        array_image.get(position).drawable
                    )
                )
            }
            else {
                binder.icon.setImageDrawable(
                    ContextCompat.getDrawable(
                        getContainerActivity(),
                        array_image.get(position).image
                    )
                )
            }
            binder.searchText.text = array_image.get(position).name


        }

        val listOfNames = ArrayList<Marketplace>()
        val listOfNames2 = ArrayList<Marketplace>()
        listOfNames.add(Marketplace(false, "All Subcategories"))
        listOfNames.add(Marketplace(true, "Arithmetic"))
        listOfNames.add(Marketplace(false, "Counting"))
        listOfNames.add(Marketplace(false, "Geometry"))
        listOfNames.add(Marketplace(false, "Algebra"))
        listOfNames.add(Marketplace(false, "Trigonometry"))

        listOfNames2.add(Marketplace(false, "Statistics & Probability"))
        listOfNames2.add(Marketplace(false, "Pre-Calculus "))
        listOfNames2.add(Marketplace(false, "Calculus"))
        listOfNames2.add(Marketplace(false, "Linear Algebra"))
        listOfNames2.add(Marketplace(false, "Differential Equations"))
        listOfNames2.add(Marketplace(false, "SAT & ACT Mathematics"))

        recursiveView(listOfNames, listOfNames2)


        val mLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        viewDataBinding.categoryRecyclerView.layoutManager = mLayoutManager
        viewDataBinding.categoryRecyclerView.adapter = mCategoryAdapter


    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_home
    }

    fun recursiveView(listOfNames: ArrayList<Marketplace>, listOfNames2: ArrayList<Marketplace>) {
        if (viewDataBinding.llViewFirst.childCount > 0) {
            viewDataBinding.llViewFirst.removeAllViews()
        }
        for (item in listOfNames) {
            val view = LayoutInflater.from(getContainerActivity()).inflate(R.layout.category_flex_list_view, null)
            val bindview =  CategoryFlexListViewBinding.bind(view)

            bindview.accounting.text = item.name
            bindview.accounting.setOnClickListener {
                for (data in listOfNames) {
                    if (data.value) {
                        data.value = false
                        break
                    }
                }
                for (data in listOfNames2) {
                    if (data.value) {
                        data.value = false
                        break
                    }
                }
                item.value = true
                recursiveView(listOfNames, listOfNames2)
            }
            if (item.value) {
                bindview.accounting.setBackgroundResource(R.drawable.select_flex_back)
                bindview.accounting.setTextColor(Color.WHITE)
            } else {
                bindview.accounting.setBackgroundResource(R.drawable.sub_caegory_flex)
                bindview.accounting.setTextColor(Color.BLACK)
            }
            viewDataBinding.llViewFirst.addView(bindview.root)
        }

        if (viewDataBinding.llViewSecond.childCount > 0) {
            viewDataBinding.llViewSecond.removeAllViews()
        }
        for (item in listOfNames2) {
            val view = LayoutInflater.from(getContainerActivity()).inflate(R.layout.category_flex_list_view, null)
            val bindview = CategoryFlexListViewBinding.bind(view)

            bindview.accounting.text = item.name

            bindview.accounting.setOnClickListener {
                for (data in listOfNames) {
                    if (data.value) {
                        data.value = false
                        break
                    }
                }
                for (data in listOfNames2) {
                    if (data.value) {
                        data.value = false
                        break
                    }
                }
                item.value = true
                recursiveView(listOfNames, listOfNames2)
            }

            if (item.value) {
                bindview.accounting.setBackgroundResource(R.drawable.select_flex_back)
                bindview.accounting.setTextColor(Color.WHITE)
            } else {
                bindview.accounting.setBackgroundResource(R.drawable.sub_caegory_flex)
                bindview.accounting.setTextColor(Color.BLACK)
            }
            viewDataBinding.llViewSecond.addView(bindview.root)
        }


    }

    private fun callGetSellerList() {
        showLoading()
        disposable.add(
            apiService.apiGetSellerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SellerList>() {
                    @SuppressLint("SetTextI18n")
                    override fun onSuccess(model: SellerList) {
                        hideLoading()
                        listUser.clear()
                        model.data!!.seller?.let { listUser.addAll(it) }
                        mSellerAdapter?.notifyDataSetChanged()

                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.d("logE", "" + e)
                    }
                })
        )
    }

}
