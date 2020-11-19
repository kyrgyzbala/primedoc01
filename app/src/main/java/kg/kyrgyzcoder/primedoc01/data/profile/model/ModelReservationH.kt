package kg.kyrgyzcoder.primedoc01.data.profile.model


import com.google.gson.annotations.SerializedName

data class ModelReservationH(
    val checkUrl: String?,
    val doctor: Doctor,
    val end: String,
    val id: Int,
    val phoneNumber: String,
    val start: String
)