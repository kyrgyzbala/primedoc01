package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.bottom

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.timessquare.CalendarPickerView
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.util.getDayOfWeek
import kg.kyrgyzcoder.primedoc01.util.getMonth
import kotlinx.android.synthetic.main.calendar_view_bottom.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class CalendarBottomView(
    private val listener: CalendarBottomClickListener,
    private val start: Date,
    private val dayCount: Int
) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.calendar_view_bottom, container, false)

        Log.d("CalendarBottomView", "onCreateView (line 32): $start")
        val nextMonth = Calendar.getInstance()
        nextMonth.time = start
        if (dayCount == 0)
            nextMonth.add(Calendar.DAY_OF_MONTH, dayCount + 1)
        else
            nextMonth.add(Calendar.DAY_OF_MONTH, dayCount)
        if (start < nextMonth.time)
            v.calendarView.init(start, nextMonth.time)
        else
            v.calendarView.init(nextMonth.time, start)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        var date = sdf.format(Date())

        val calendarr = Calendar.getInstance()
        val dayOfWeekr = calendarr[Calendar.DAY_OF_WEEK]
        val weekr = getDayOfWeek(dayOfWeekr, v.context)
        val mr = getMonth(calendarr[Calendar.MONTH], v.context)
        v.yearTextViewCalendar.text = calendarr[Calendar.YEAR].toString()

        val doyMr = if (calendarr[Calendar.DAY_OF_MONTH] < 10)
            "0${calendarr[Calendar.DAY_OF_MONTH]}"
        else
            "${calendarr[Calendar.DAY_OF_MONTH]}"

        var datee = "$weekr, $doyMr $mr"
        v.dateTextViewCalendar.text = datee
        v.calendarView.setOnDateSelectedListener(object :
            CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(dat: Date?) {
                val calendaro = Calendar.getInstance()
                calendaro.time = dat!!
                val year = calendaro.get(Calendar.YEAR)
                val month = calendaro.get(Calendar.MONTH)
                val day = calendaro.get(Calendar.DAY_OF_MONTH)

                val dayOfWeek = calendaro[Calendar.DAY_OF_WEEK]

                val week = getDayOfWeek(dayOfWeek, v.context)
                val m = getMonth(month, v.context)

                val monM = if ((month + 1) < 10)
                    "0${month + 1}"
                else
                    "${month + 1}"
                val doyM = if (day < 10)
                    "0$day"
                else
                    "$day"
                date = "$year-$monM-$doyM"
                v.yearTextViewCalendar.text = year.toString()
                datee = "$week, $doyM $m"
                v.dateTextViewCalendar.text = datee
            }

            override fun onDateUnselected(date: Date?) {
            }

        })

        v.okButtonCalendarView.setOnClickListener {
            listener.onClickOk(date, datee)
            dismiss()
        }

        v.cancelButtonCalendarView.setOnClickListener {
            listener.onClickCancel()
            dismiss()
        }

        return v
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    interface CalendarBottomClickListener {
        fun onClickOk(date: String, datee: String)
        fun onClickCancel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheet =
                (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }

        return dialog
    }
}