package kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation


import com.google.gson.annotations.SerializedName

data class ModelMakeReservation(
    val clientId: Int,
    val comment: String,
    val end: String,
    val phoneNumber: String,
    val start: String,
    val worktimeId: Int
)