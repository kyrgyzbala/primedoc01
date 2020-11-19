package kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response


import com.google.gson.annotations.SerializedName

data class ChannelEntity(
    val active: Boolean,
    val client: Client,
    val clientIdentity: String,
    val doctor: Doctor,
    val doctorIdentity: String,
    val id: Int,
    val sid: String
)