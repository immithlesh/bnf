package com.application.brainnforce.ui.fragments.packages

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.application.brainnforce.R
import com.application.brainnforce.databinding.FragCreatePackageBinding
import com.application.brainnforce.model.*
import com.application.brainnforce.ui.fragments.category.SignupCategoryFragment
import com.application.brainnforce.utils.content
import com.application.swapp.app.BNFBaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type

class CreatePackageFragment(
    var editProfle: Int,
    var idMain: Int?,
    var titleMain: String?
) : BNFBaseFragment<FragCreatePackageBinding>() {


    var packageType = arrayOf<String?>("Bronze", "Silver", "Gold")
    var duration = arrayOf<String?>("Days", "Month", "Year")
    var subCategory: List<SubCategoriesList>? = null
    var keyFeatures: ArrayList<KeyFeature> = ArrayList()
    var packageText = ""
    var durationText = ""
    var stringList = ""
    var singleComment = ""
    var edittTxt: EditText? = null
    var textView: TextView? = null
    private var hint = 0
    private var user: UserPackages? = null

    override fun getLayoutId(): Int {
        return R.layout.frag_create_package
    }

    override fun getCurrentFragment(): Fragment {
        return this
    }

    override fun configureToolbar(): Any? {
        return null
    }

    override fun isToolbarVisible(): Boolean {
        return false
    }

    override fun isBottomBarVisible(): Boolean {
        return false
    }

    private val keys: ArrayList<String> = ArrayList()
    val keyJA = JSONArray()

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.addMore.setOnClickListener {
            createEditTextView()
        }

        if (editProfle == 1) {
            viewDataBinding.textView7.text = getString(R.string.editPackage)
            viewDataBinding.submitBtn.text = getString(R.string.save_chnge)
        } else {
            viewDataBinding.textView7.text = getString(R.string.createPackage)
            viewDataBinding.submitBtn.text = getString(R.string.submit)
        }

        if (editProfle == 1) {

            user = linkodesPref.retrievePackageInfo(getContainerActivity())!!

            viewDataBinding.createPackDescription.setText(user!!.description)
            viewDataBinding.createPackPrice.setText(user!!.price.toString())
            viewDataBinding.createPackConcepts.setText(user!!.revision)
            viewDataBinding.createPackDuration.setText(user!!.duration!!.toString())

            durationText = user!!.durationUnit!!
            if (durationText.equals("Days")) {
                viewDataBinding.createPackDurDay.setSelection(0)

            } else if (durationText.equals("Month")) {
                viewDataBinding.createPackDurDay.setSelection(1)

            } else if (durationText.equals("Year")) {
                viewDataBinding.createPackDurDay.setSelection(2)
            }

            packageText = user!!.title!!
            if (packageText.equals("Bronze")) {
                viewDataBinding.packageSpinner.setSelection(0)
            } else if (packageText.equals("Silver")) {
                viewDataBinding.packageSpinner.setSelection(1)
            } else if (packageText.equals("Gold")) {
                viewDataBinding.packageSpinner.setSelection(2)
            }


        }

        viewDataBinding.backIcon.setOnClickListener {
            getContainerActivity().onBackPressed()
        }

        viewDataBinding.createPackCategory.setOnClickListener {
            displayIt(
                SignupCategoryFragment(),
                SignupCategoryFragment::class.java.canonicalName,
                true
            )
        }

        viewDataBinding.submitBtn.setOnClickListener {

            keys.add(edittTxt!!.text.toString())

            for (key in keys) {

                Log.e("KEY", key)
                keyJA.put(JSONObject().put("keyFeature", key))

            }

            val type: Type = object : TypeToken<List<SubCategoriesList?>?>() {}.type
            keyFeatures = Gson().fromJson(keyJA.toString(), type)

            if (validateEditText(viewDataBinding.titleText)
                && validateEditText(viewDataBinding.createPackDescription)
                && validateEditText(viewDataBinding.createPackPrice)
                && validateEditText(viewDataBinding.createPackDuration)
                && validateEditText(viewDataBinding.createPackConcepts)
//                && validateEditText(viewDataBinding.TvSourceFile)
            ) {
                if (editProfle == 1) {
                    callEditPackage()
                } else {
                    callCreatePackage()
                }
            }

        }

        if (titleMain != null) {
            viewDataBinding.titleText.setText(titleMain!!.toString())
        }

        setPackageSpinner()
        setDurationSpinner()

        if (editProfle == 1) {
            if (!linkodesPref.getSaveValue(getContainerActivity(), "Category2").isBlank()) {
                stringList = linkodesPref.getSaveValue(getContainerActivity(), "Category2")
                val type: Type = object : TypeToken<List<SubCategoriesList?>?>() {}.type
                subCategory = Gson().fromJson(stringList, type)

                Log.e("SIZE", subCategory?.size.toString())

                if (subCategory?.size!! > 0) {
                    viewDataBinding.createPackCategory.text =
                        subCategory!!.size.toString() + " Categories Selected"
                } else {
                    viewDataBinding.createPackCategory.text =
                        user!!.userPackageSubCategoriesList!!.size.toString() + " Categories Selected"
                }
            } else {
                viewDataBinding.createPackCategory.text =
                    user!!.userPackageSubCategoriesList!!.size.toString() + " Categories Selected"
            }
        } else {
            if (!linkodesPref.getSaveValue(getContainerActivity(), "Category2").isBlank()) {
                stringList = linkodesPref.getSaveValue(getContainerActivity(), "Category2")
                val type: Type = object : TypeToken<List<SubCategoriesList?>?>() {}.type
                subCategory = Gson().fromJson(stringList, type)

                Log.e("SIZE", subCategory?.size.toString())

                if (subCategory?.size!! > 0) {
                    viewDataBinding.createPackCategory.text =
                        subCategory!!.size.toString() + " Categories Selected"
                } else {
                    viewDataBinding.createPackCategory.text = "Select Categories"
                }
            }
        }

    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun createEditTextView() {

        if (edittTxt != null)
            keys.add(edittTxt!!.text.toString())

        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        params.setMargins(0, 10, 0, 10)

        edittTxt = EditText(getContainerActivity())
//        val maxLength = 5
        hint++
        edittTxt!!.hint = "Ex.Source File Hand Over"
        edittTxt!!.layoutParams = params
        edittTxt!!.setBackgroundResource(R.drawable.edit_text_background)
        edittTxt!!.inputType = InputType.TYPE_CLASS_TEXT
        edittTxt!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.7f)
        edittTxt!!.setPadding(45, 45, 45, 45)
//        edittTxt!!.setTypeface(edittTxt!!.getTypeface(), Typeface.BOLD)
        edittTxt!!.setTextAppearance(R.style.TextStyleHelvetica)
        edittTxt!!.id = hint
        textView = TextView(getContainerActivity())
        textView!!.text = "Delete"
        textView!!.setTextColor(resources.getColor(R.color.red))
        textView!!.gravity = Gravity.END
        textView!!.textSize = 14F
        textView!!.typeface = resources.getFont(R.font.helvetica_bold)
        viewDataBinding.editTextContainer.addView(edittTxt)
        viewDataBinding.editTextContainer.addView(textView)



    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        createEditTextView()
    }

    private fun setPackageSpinner() {
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            getContainerActivity(),
            android.R.layout.simple_spinner_item,
            packageType
        )

        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        viewDataBinding.packageSpinner.adapter = ad

        viewDataBinding.packageSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                view: View?,
                position: Int,
                arg3: Long
            ) {
                Log.e("STng", "" + packageType[position])
                packageText = packageType[position]!!
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }

    private fun setDurationSpinner() {

        val ad1: ArrayAdapter<*> = ArrayAdapter<Any?>(
            getContainerActivity(),
            android.R.layout.simple_spinner_item,
            duration
        )

        ad1.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        viewDataBinding.createPackDurDay.adapter = ad1

        viewDataBinding.createPackDurDay.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                view: View?,
                position: Int,
                arg3: Long
            ) {
                Log.e("STng", "" + duration[position])
                durationText = duration[position]!!
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }

    private fun callCreatePackage() {
        viewDataBinding.apply {

            val userPackages = UserPackage()
            userPackages.title = packageText
            userPackages.price = viewDataBinding.createPackPrice.content
            userPackages.duration = viewDataBinding.createPackDuration.content
            userPackages.durationUnit = durationText
            userPackages.description = viewDataBinding.createPackDescription.content
            userPackages.revision = viewDataBinding.createPackConcepts.content
            userPackages.subCategories = subCategory
            userPackages.keyFeatures = keyFeatures

            val editCategory = EditCategory()
            editCategory.id = idMain ?: ""
            if (titleMain.equals("")) {
                editCategory.title = viewDataBinding.titleText.content
            } else {
                editCategory.title = titleMain
            }
            editCategory.userPackage = userPackages

            showLoading()
            disposable.add(
                apiService.apiuserPackage(editCategory)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Any>() {

                        override fun onSuccess(model: Any) {
                            hideLoading()
                            showToast("Package Created Successfully")
                            getContainerActivity().onBackPressed()
                        }

                        override fun onError(e: Throwable) {
                            hideLoading()
                        }
                    })
            )
        }

    }

    private fun callEditPackage() {
        viewDataBinding.apply {

            val userPackages = UserPackage()
            userPackages.title = packageText
            userPackages.price = viewDataBinding.createPackPrice.content
            userPackages.duration = viewDataBinding.createPackDuration.content
            userPackages.durationUnit = durationText
            userPackages.description = viewDataBinding.createPackDescription.content
            userPackages.revision = viewDataBinding.createPackConcepts.content
            userPackages.subCategories = subCategory
            userPackages.keyFeatures = keyFeatures

            val editCategory = EditCategory()
            editCategory.id = idMain ?: ""
            if (titleMain.equals("")) {
                editCategory.title = viewDataBinding.titleText.content
            } else {
                editCategory.title = titleMain
            }
            editCategory.userPackage = userPackages

            showLoading()
            disposable.add(
                apiService.apiUpdatePackage(editCategory)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Any>() {

                        override fun onSuccess(model: Any) {
                            hideLoading()
                            showToast("Package Updated Successfully")
                            getContainerActivity().onBackPressed()
                        }

                        override fun onError(e: Throwable) {
                            hideLoading()
                        }
                    })
            )
        }

    }

}
