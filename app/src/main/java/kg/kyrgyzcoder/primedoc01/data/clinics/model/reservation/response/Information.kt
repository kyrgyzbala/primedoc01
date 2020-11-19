package kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response


import com.google.gson.annotations.SerializedName

data class Information(
    val end: String,
    val id: Int,
    val infoType: String,
    val name: String,
    val organizationName: String,
    val start: String
)