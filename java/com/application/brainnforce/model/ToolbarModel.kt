package com.application.brainnforce.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class ToolbarModel(val title: String?)
class ArrayImage(val drawable: Int, var value: Boolean, val image: Int, val name: String)
class Marketplace(var value: Boolean, val name: String)


class UserDetail() : Parcelable {
    var name: String = ""
    var description: String = ""
    var notes: Int = 0

    constructor(parcel: Parcel) : this() {
        name = parcel.readString() ?: ""
        description = parcel.readString() ?: ""
        notes = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(notes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDetail> {
        override fun createFromParcel(parcel: Parcel): UserDetail {
            return UserDetail(parcel)
        }

        override fun newArray(size: Int): Array<UserDetail?> {
            return arrayOfNulls(size)
        }

    }

}

/*
Login full details
 */

class LoginCrediental {
    var data: Data? = null
    var message: String? = null
}

class Data {
    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("user")
    var user: User? = null

}

class Role {

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("role")
    var role: String? = null
}

class User {

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("firstname")
    var firstname: String? = null

    @SerializedName("lastname")
    var lastname: String? = null

    @SerializedName("gender")
    var gender: String? = null

    @SerializedName("role")
    var role: Role? = null

    @SerializedName("dateofbirth")
    var dateofbirth: String? = null

    @SerializedName("mobile")
    var mobile: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("profileImagePath")
    var profileImagePath: String? = null

    @SerializedName("status")
    var status: Boolean? = null

    @SerializedName("location")
    var location: String? = null

    @SerializedName("bio")
    var bio: String? = null

    @SerializedName("latitude")
    var latitude: String? = null

    @SerializedName("longitude")
    var longitude: String? = null

    @SerializedName("coverImagePath")
    var coverImagePath: String? = null

    @SerializedName("coverVideoPath")
    var coverVideoPath: String? = null

    @SerializedName("createdDate")
    var createdDate: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("socialMediaId")
    var socialMediaId: String? = null

    @SerializedName("socialMediaType")
    var socialMediaType: String? = null

    @SerializedName("idProofPath")
    var idProofPath: String? = null

    @SerializedName("totalExperience")
    var totalExperience: String? = null

    @SerializedName("hourlyPriceForVideoConfercing")
    var hourlyPriceForVideoConfercing: String? = null

    @SerializedName("lastPasswordResetDate")
    var lastPasswordResetDate: String? = null

    @SerializedName("languages")
    var languages: String? = null

    @SerializedName("userCategoriesList")
    var userCategoriesList: List<UserCategories>? = null

    @SerializedName("userParentPackageList")
    var userParentPackageList: List<UserParentPackage>? = null

}

class UserCategories {
    val category: Any? = null
    val id: Int? = null
    val subCategory: SubCategoryXX? = null
}

class UserParentPackage {
    val id: Int? = null
    val title: String? = null
    val userPackagesList: List<UserPackages>? = null
}

class SubCategoryXX {
    val id: Int? = null
    val imagePath: Any? = null
    val status: Boolean? = null
    val subCategoryName: String? = null
}

class UserPackages {
    val description: String? = null
    val duration: Int? = null
    val durationUnit: String? = null
    val id: Int? = null
    val price: Float? = null
    val revision: String? = null
    val title: String? = null
    val userPackageKeyFeaturesList: List<UserPackageKeyFeatures>? = null
    val userPackageSubCategoriesList: List<UserPackageSubCategories>? = null
}

class UserPackageKeyFeatures {
    val id: Int? = null
    val keyFeature: String? = null
}

class UserPackageSubCategories {
    val id: Int? = null
    val subCategory: SubCategoryX? = null
}

class SubCategoryX {
    val id: Int? = null
    val imagePath: Any? = null
    val status: Boolean? = null
    val subCategoryName: String? = null
}


/*
Login
 */
class LoginRequestModel(
    var email: String? = null,
    var password: String? = null,
    var roleId: String? = null
)

/*
Create Account
 */
class SignUpSellerRequestModel {
    var dateofbirth: String? = null
    var email: String? = null
    var firstname: String? = null
    var gender: String? = null
    var languages: String? = null
    var lastname: String? = null
    var latitude: String? = null
    var location: String? = null
    var longitude: String? = null
    var mobile: String? = null
    var password: String? = null
    var role_id: String? = null
    var subCategoriesList: List<SubCategoriesList>? = null
    var totalExperience: String? = null
}

class SignUpSellerRequestModel1 {
    var dateofbirth: String? = null
    var email: String? = null
    var firstname: String? = null
    var gender: String? = null
    var lastname: String? = null
    var latitude: String? = null
    var location: String? = null
    var longitude: String? = null
    var mobile: String? = null
    var password: String? = null
    var role_id: String? = null
    var profilePicture: File? = null
    var idProof: File? = null
}

class SubCategories {
    var id: Int? = null
}

/*
Change password
 */
class changePassword {
    var currentPassword: String? = null
    var newPassword: String? = null

}


/*
Get Category Details
*/

class MainCategory {
    @SerializedName("data")
    var data: CategoryData? = null
}

class CategoryData {
    @SerializedName("categories")
    var categories: ArrayList<Categories>? = null
}

class Categories {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("categoryName")
    var categoryName: String? = null

    @SerializedName("status")
    var status: Boolean? = null

    @SerializedName("imagePath")
    var imagePath: String? = null

    @SerializedName("subCategoriesList")
    var subCategoriesList: ArrayList<SubCategoriesList>? = null
}

class SubCategoriesList {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("subCategoryName")
    var subCategoryName: String? = null

    @SerializedName("status")
    var status: Boolean? = null

    @SerializedName("imagePath")
    var imagePath: String? = null


}

//class Cate {
//
//    var int : id ?  = null
//    var String : status? = null
//    var String :  subCategoryName? = null
//
//}

/*
Create Package
 */

class EditCategory {
    var id: Any? = null
    var title: String? = null
    var userPackage: UserPackage? = null
}

class UserPackage {
    var description: String? = null
    var duration: String? = null
    var durationUnit: String? = null
    var keyFeatures: ArrayList<KeyFeature>? = null
    var price: String? = null
    var revision: String? = null
    var subCategories: List<SubCategoriesList>? = null
    var title: String? = null
}


class KeyFeature {
    var keyFeature: String? = null
}

class SubCategory {
    var id: Int? = null
}


///////////SellerList
class SellerList {
    val `data`: Data3? = null
}

class Data3 {
    val seller: List<Seller>? = null
}

class Seller {
    val bio: Any? = null
    val coverImagePath: Any? = null
    val coverVideoPath: Any? = null
    val createdDate: Any? = null
    val dateofbirth: String? = null
    val description: Any? = null
    val email: String? = null
    val firstname: String? = null
    val gender: String? = null
    val hourlyPriceForVideoConfercing: Any? = null
    val id: Int? = null
    val idProofPath: String? = null
    val languages: String? = null
    val lastPasswordResetDate: Any? = null
    val lastname: String? = null
    val latitude: String? = null
    val location: String? = null
    val longitude: String? = null
    val mobile: String? = null
    val password: String? = null
    val profileImagePath: String? = null
    val role: Role? = null
    val socialMediaId: Any? = null
    val socialMediaType: Any? = null
    val status: Boolean? = null
    val totalExperience: Int? = null
    val userCategoriesList: List<UserCategories>? = null
    val userParentPackageList: List<UserParentPackage>? = null
}

class Role3 {
    val id: Int? = null
    val role: String? = null
}

class UserCategories3 {
    val category: Any? = null
    val id: Int? = null
    val subCategory: SubCategory? = null
}

class UserParentPackage3 {
    val id: Int? = null
    val status: Boolean? = null
    val title: String? = null
    val userPackagesList: List<UserPackages>? = null
}

class SubCategory3 {
    val id: Int? = null
    val imagePath: Any? = null
    val status: Boolean? = null
    val subCategoryName: String? = null
}

class UserPackages3 {
    val description: String? = null
    val duration: Double? = null
    val durationUnit: Any? = null
    val id: Int? = null
    val price: Double? = null
    val revision: String? = null
    val status: Boolean? = null
    val title: String? = null
    val userPackageKeyFeaturesList: List<UserPackageKeyFeatures>? = null
    val userPackageSubCategoriesList: List<UserPackageSubCategories>? = null
}

class UserPackageKeyFeatures3 {
    val id: Int? = null
    val keyFeature: String? = null
}

class UserPackageSubCategories3 {
    val id: Int? = null
    val subCategory: SubCategoryX? = null
}

class SubCategoryX3 {
    val id: Int? = null
    val imagePath: Any? = null
    val status: Boolean? = null
    val subCategoryName: String? = null
}
