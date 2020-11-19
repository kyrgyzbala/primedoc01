package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.bottom

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.clinics.model.worktime.Reservation
import kotlinx.android.synthetic.main.calendar_view_bottom_time.view.*
import kotlinx.android.synthetic.main.row_time.view.*

class WorkTimes(
    private val reservations: List<Reservation>,
    private val year: Int,
    private val datee: String,
    private val listener: WorkTimeClickListener
) : BottomSheetDialogFragment(),
    TimeRecyclerViewAdapter.TimeClickListener {

    private lateinit var adapter: TimeRecyclerViewAdapter
    private var currentSelected: Reservation? = null
    private lateinit var recyclerView: RecyclerView
    private var selectedPos: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.calendar_view_bottom_time, container, false)

        view.yearTextViewCalendar.text = year.toString()
        view.dateTextViewCalendar.text = datee
        val txt = "$datee, $year"
        view.dateTextViewTime.text = txt
        recyclerView = view.findViewById(R.id.recyclerViewTime)

        view.recyclerViewTime.setHasFixedSize(true)
        adapter = TimeRecyclerViewAdapter(this)
        view.recyclerViewTime.adapter = adapter
        adapter.submitList(reservations)

        view.okButtonCalendarViewTime.setOnClickListener {
            if (currentSelected != null) {
                listener.onOkClick(currentSelected!!)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Пожалуйста выберите время или другую дату!", Toast.LENGTH_SHORT).show()
            }
        }
        view.cancelButtonCalendarViewTime.setOnClickListener {
            listener.onCancelClick()
            dismiss()
        }

        view.arrBackTime.setOnClickListener {
            listener.onBackClick()
            dismiss()
        }


        return view
    }

    override fun onClick(position: Int) {
        if (selectedPos != null) {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(selectedPos!!)

            viewHolder?.itemView?.timeCardViewRow?.setCardBackgroundColor(Color.parseColor("#1A00BDD0"))
            viewHolder?.itemView?.textViewTimeRow?.setTextColor(Color.parseColor("#00BDD0"))
        }
        currentSelected = adapter.getItemAtPos(position)
        selectedPos = position
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    interface WorkTimeClickListener {
        fun onOkClick(selected: Reservation)
        fun onCancelClick()
        fun onBackClick()
    }
}