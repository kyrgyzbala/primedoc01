package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.bottom

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.clinics.model.worktime.Reservation
import kotlinx.android.synthetic.main.row_clinic.view.*
import kotlinx.android.synthetic.main.row_time.view.*

class TimeRecyclerViewAdapter(private val listener: TimeClickListener) :
    ListAdapter<Reservation, TimeRecyclerViewAdapter.TViewHolder>(DIFF) {

    fun getItemAtPos(position: Int): Reservation {
        return getItem(position)
    }

    inner class TViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)

            val text = "${current.start?.take(5)}"
            var hour = text.take(2).toInt() + 6
            if (hour > 23)
                hour -= 24
            val hourStr = if (hour < 10)
                "0$hour"
            else
                hour.toString()
            val timeToShow = "$hourStr${text.takeLast(3)}"

            itemView.textViewTimeRow.text = timeToShow

            if (current.id == null) {
                itemView.timeCardViewRow.setCardBackgroundColor(Color.parseColor("#1A00BDD0"))
            } else {
                itemView.timeCardViewRow.setCardBackgroundColor(Color.parseColor("#56D00000"))
            }

            itemView.setOnClickListener {
                if (current.id == null) {
                    itemView.timeCardViewRow.setCardBackgroundColor(Color.parseColor("#00BDD0"))
                    itemView.textViewTimeRow.setTextColor(Color.parseColor("#FFFFFF"))
                    listener.onClick(position)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TViewHolder {
        return TViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_time, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TViewHolder, position: Int) {
        holder.onBind(position)
    }

    interface TimeClickListener {
        fun onClick(position: Int)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Reservation>() {
            override fun areItemsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}