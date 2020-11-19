package kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response


import com.google.gson.annotations.SerializedName

data class ModelReservResponse(
    val end: String,
    val id: Int,
    val reservation: List<Reservation>,
    val start: String
)