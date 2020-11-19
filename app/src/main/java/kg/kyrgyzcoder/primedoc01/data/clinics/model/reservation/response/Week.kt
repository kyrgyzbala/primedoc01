package kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response


import com.google.gson.annotations.SerializedName

data class Week(
    val id: Int,
    val weekDays: List<WeekDay>,
    val weekOrder: Int
)