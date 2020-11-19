package kg.kyrgyzcoder.primedoc01.data.clinics.model.worktime


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Reservation(
    val channelEntity: Any,
    val comment: String?,
    val end: String?,
    val id: Int?,
    val paid: Boolean,
    val phoneNumber: String?,
    val start: String?
): Serializable