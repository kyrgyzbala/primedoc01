package kg.kyrgyzcoder.primedoc01.data.clinics.model.detail


import com.google.gson.annotations.SerializedName

data class ModelCategDetail(
    val description: String?,
    val doctors: List<Doctor>,
    val id: Int,
    val illnesses: List<Any>,
    val image: String?,
    val name: String?
)