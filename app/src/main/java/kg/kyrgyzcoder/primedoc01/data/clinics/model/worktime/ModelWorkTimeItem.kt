package kg.kyrgyzcoder.primedoc01.data.clinics.model.worktime


import com.google.gson.annotations.SerializedName

data class ModelWorkTimeItem(
    var end: String,
    val id: Int,
    val reservation: MutableList<Reservation>,
    var start: String
)