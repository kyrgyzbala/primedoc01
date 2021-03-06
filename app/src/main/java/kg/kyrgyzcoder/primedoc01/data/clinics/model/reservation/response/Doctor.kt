package kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response


import com.google.gson.annotations.SerializedName

data class Doctor(
    val bio: String,
    val channels: List<Any>,
    val id: Int,
    val image: String,
    val information: List<Information>,
    val position: String,
    val schedules: List<Schedule>
)