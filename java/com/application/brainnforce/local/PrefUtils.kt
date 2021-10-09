package com.application.brainnforce.local

import android.content.Context
import android.content.SharedPreferences
import com.application.brainnforce.model.SignUpSellerRequestModel1
import com.application.brainnforce.model.SubCategories
import com.application.brainnforce.model.User
import com.application.brainnforce.model.UserPackages
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefUtils {

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("LINKODES_PREF", Context.MODE_PRIVATE)
    }

    fun storeAuthKey(context: Context, apiKey: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString("AUTH_KEY", apiKey)
        editor.apply()
    }

    fun getAuthKey(context: Context): String? {
        return getSharedPreferences(context).getString("AUTH_KEY", null)
    }


    fun setSaveValue(context: Context, key: String, value: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setSaveValue1(context: Context, key: String, value: Int?) {
        val editor = getSharedPreferences(context).edit()
        editor.putInt(key, value ?: 0)
        editor.apply()
    }

    fun getSaveValue(context: Context, key: String): String {

        if (getSharedPreferences(context).getString(key, "")!!.isEmpty()) {
            return ""
        } else {
            return getSharedPreferences(context).getString(key, "")!!
        }
    }

    fun getSaveIntValue(context: Context, key: String): Int {
        return getSharedPreferences(context).getInt(key, 0)
    }


    fun storeUserData(context: Context, user: SignUpSellerRequestModel1) {
        val editor = getSharedPreferences(context).edit()
        editor.putString("LinkodesModel_User", Gson().toJson(user))
        editor.apply()
    }

    fun getUserData(context: Context): SignUpSellerRequestModel1? {
        val type = object : TypeToken<SignUpSellerRequestModel1>() {}.type
        return Gson().fromJson(
            getSharedPreferences(context).getString("LinkodesModel_User", null),
            type
        )
    }

    fun storeUserInfo(context: Context, user: User) {
        val editor = getSharedPreferences(context).edit()
        editor.putString("USER_INFO", Gson().toJson(user))
        editor.apply()
    }


    fun retrieveUserInfo(context: Context): User? {
        val type = object : TypeToken<User>() {}.type
        return Gson().fromJson(getSharedPreferences(context).getString("USER_INFO", null), type)
    }

    fun setUserLoggedInStatus(context: Context, isLogin: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean("IS_USER_LOGGED_IN", isLogin)
        editor.apply()
    }

    fun getUserLoggedInStatus(context: Context): Boolean? {
        return getSharedPreferences(context).getBoolean("IS_USER_LOGGED_IN", false)
    }

    fun clearPrefs(context: Context) {
        val sharedPrefs = context.getSharedPreferences("LINKODES_PREF", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.clear()
        editor.apply()
    }

    fun clear(context: Context, name: String) {
        val sharedPrefs = context.getSharedPreferences("LINKODES_PREF", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.remove(name)
        editor.apply()
    }


    fun storePackageInfo(context: Context, user: UserPackages) {
        val editor = getSharedPreferences(context).edit()
        editor.putString("USER_INFO1", Gson().toJson(user))
        editor.apply()
    }


    fun retrievePackageInfo(context: Context): UserPackages? {
        val type = object : TypeToken<UserPackages>() {}.type
        return Gson().fromJson(getSharedPreferences(context).getString("USER_INFO1", null), type)
    }


    fun storeCategoryInfo(context: Context, user: SubCategories) {
        val editor = getSharedPreferences(context).edit()
        editor.putString("USER_INFO2", Gson().toJson(user))
        editor.apply()
    }

    fun retrieveCategoryInfo(context: Context): SubCategories {
        val type = object : TypeToken<ArrayList<SubCategories>>() {}.type
        return Gson().fromJson(
            getSharedPreferences(context).getString("USER_INFO2", null),
            type
        )
    }


}