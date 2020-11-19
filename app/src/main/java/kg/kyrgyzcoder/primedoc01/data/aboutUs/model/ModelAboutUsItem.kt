package kg.kyrgyzcoder.primedoc01.data.aboutUs.model


import com.google.gson.annotations.SerializedName

data class ModelAboutUsItem(
    val header: String,
    val id: Int,
    val order: Int,
    val paragraph: String
)