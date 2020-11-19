package kg.kyrgyzcoder.primedoc01.util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kg.kyrgyzcoder.primedoc01.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


/**
 * Shared pref keys
 */

const val USER_STATUS = "USER_STATUS"
const val USER_STATUS_KEY = "USER_STATUS_KEy"

const val USER_CREDENTIALS = "USER_CREDENTIALS"
const val USER_PHONE = "USER_PHONE"
const val USER_PWD = "USR_PWD"

const val USER_PIN = "USER_PIN"
const val USER_PIN_KEY = "USER_PIN_KEY"

const val USER_CRED = "USER_CRED"
const val USER_ID_KEY = "USER_ID_KEY"
const val CHAT_ID_KEY = "CHAT_ID_KEY"
const val USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN"
const val USER_CHAT_TOKEN = "USER_CHAT_TOKEN"
const val USER_REFRESH_TOKEN = "USER_REFRESH_TOKEN"
const val USER_CHAT_EXP = "USER_CHAT_EXP"
const val USER_REFRESH_EXP = "USER_REFRESH_EXP"

const val DOCTOR_DATA = "DOCTOR_DATA"
const val DOCTOR_NAME_KEY = "DOCTOR_NAME_KEY"
const val DOCTOR_SURNAME_KEY = "DOCTOR_SURNAME_KEY"

/**
 * EXTRAS for intents
 */

const val EXTRA_HTML = "EXTRA_HTML"
const val EXTRA_TITLE_DOC = "EXTRA_TITLE_DOC"

const val EXTRA_FROM_PIN = "EXTRA_FROM_PIN"

const val EXTRA_FROM_NOTIF = "EXTRA_FROM_NOTIF"

const val EXTRA_USER_PHONE = "EXTRA_USER_PHONE"
const val EXTRA_USER_PWD = "EXTRA_USER_PWD"
const val EXTRA_CODE_TYPE = "EXTRA_CODE_TYPE"

const val EXTRA_PIN = "EXTRA_PIN"

const val EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID"
const val EXTRA_DOCTOR_ID = "EXTRA_DOCTOR_ID"
const val EXTRA_DOCTOR_USER_ID = "EXTRA_DOCTOR_USER_ID"

const val EXTRA_MED_ADD_EDIT = "EXTRA_MED_ADD_EDIT"

const val EXTRA_RESERVATION = "EXTRA_RESERVATION"
const val EXTRA_WORK_ID = "EXTRA_WORK_ID"

const val EXTRA_CHAT_REF = "EXTRA_CHAT_REF"
const val EXTRA_CHAT_TYPE = "EXTRA_CHAT_TYPE"

const val EXTRA_IMG_URL = "EXTRA_IMG_URL"
const val EXTRA_CALL_TYPE = "EXTRA_CALL_TYPE"

const val EXTRA_RECEIVE_ID = "EXTRA_RECEIVE_ID"
const val EXTRA_CALL_REF = "EXTRA_CALL_REF"
const val EXTRA_DOC_NAME = "EXTRA_DOC_NAME"

const val REQUEST_CODE_IMG = 11
const val CHOOSE_IMAGE_REQUEST = 112

const val EXTRA_MEDCARD_INFO = "EXTRA_MEDCARD_INFO"
const val EXTRA_EDIT_MED_CARD = "EXTRA_EDIT_MED_CARD"

const val EXTRA_PAYMENT_OPTION = "EXTRA_PAYMENT_OPTION"
const val EXTRA_RES_ID = "EXTRA_RES_ID"
const val EXTRA_CARDHOLDER = "EXTRA_CARDHOLDER"
const val EXTRA_CARD_NUMBER = "EXTRA_CARD_NUMBER"

const val EXTRA_CHECK = "EXTRA_CHECK"

/**
 * Pay option colors
 */
const val MEGA_PAY = "#42C075"
const val O_PAY = ""
const val BALANCE_KG = ""
const val VISA_PAY = ""
const val MASTER_CARD_PAY = "#FFC700"


@RequiresApi(Build.VERSION_CODES.O)
fun getNewDate(dateStr: String?): LocalDateTime {
    val date =
        LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    val zonedDateTime = date.atZone(ZoneId.of("Asia/Bishkek"))
    val time = zonedDateTime.toEpochSecond() + 21600
    val newDate =
        LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault())
    Log.d(
        "NewsViewHolder",
        "initData (line 79): date: ${newDate.year}-${newDate.monthValue}-${newDate.dayOfMonth} time ${newDate.hour}:${newDate.minute} "
    )
    return newDate
}


//Functions to hide keyBoard easily aster typing is done
//hideKeyboard start...
fun Fragment.hideKeyboard() {
    view?.let {
        activity?.hideKeyboard(it)
    }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
//hideKeyboard end...

fun ContentResolver.getFileName(uri: Uri): String {
    var name = ""

    val cursor = query(uri, null, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}

fun getDayOfWeek(dayOfWeek: Int, context: Context): String {

    return when (dayOfWeek) {
        1 -> context.getString(R.string.sun)
        2 -> context.getString(R.string.mon)
        3 -> context.getString(R.string.tue)
        4 -> context.getString(R.string.wed)
        5 -> context.getString(R.string.thu)
        6 -> context.getString(R.string.fri)
        else -> context.getString(R.string.sat)
    }
}

fun getMonth(day: Int, context: Context): String {

    return when (day) {

        0 -> context.getString(R.string.jan)
        1 -> context.getString(R.string.feb)
        2 -> context.getString(R.string.march)
        3 -> context.getString(R.string.apr)
        4 -> context.getString(R.string.may)
        5 -> context.getString(R.string.june)
        6 -> context.getString(R.string.july)
        7 -> context.getString(R.string.aug)
        8 -> context.getString(R.string.sep)
        9 -> context.getString(R.string.oct)
        10 -> context.getString(R.string.nov)
        else -> context.getString(R.string.dec)
    }

}