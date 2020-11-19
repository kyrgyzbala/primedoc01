package kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response


import com.google.gson.annotations.SerializedName

data class Schedule(
    val currentWeek: Int,
    val id: Int,
    val weekDuration: Int,
    val weeks: List<Week>
)