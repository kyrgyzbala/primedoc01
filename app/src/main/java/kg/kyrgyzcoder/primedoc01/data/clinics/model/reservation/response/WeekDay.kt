package kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response


import com.google.gson.annotations.SerializedName

data class WeekDay(
    val id: Int,
    val intervals: List<Interval>,
    val weekDayName: String
)