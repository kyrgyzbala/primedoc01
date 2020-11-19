package kg.kyrgyzcoder.primedoc01.data.medcard


import com.google.gson.annotations.SerializedName

data class ModelUserPhotoResponse(
    val id: Int,
    val image: String?,
    val medCardPhoneNumber: String
)