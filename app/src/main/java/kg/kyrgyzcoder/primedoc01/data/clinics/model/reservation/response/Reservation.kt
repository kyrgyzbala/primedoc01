package kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response


import com.google.gson.annotations.SerializedName

data class Reservation(
    val channelEntity: ChannelEntity,
    val comment: String,
    val end: String,
    val id: Int,
    val paid: Boolean,
    val phoneNumber: String,
    val start: String
)