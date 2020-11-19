package kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response


import com.google.gson.annotations.SerializedName

data class Interval(
    val end: String,
    val id: Int,
    val start: String
)