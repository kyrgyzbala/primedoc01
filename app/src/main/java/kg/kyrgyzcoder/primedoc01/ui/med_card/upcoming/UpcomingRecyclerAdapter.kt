package kg.kyrgyzcoder.primedoc01.ui.med_card.upcoming

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.profile.model.ModelReservationH
import kg.kyrgyzcoder.primedoc01.ui.med_card.CheckClickListener
import kotlinx.android.synthetic.main.row_upcoming.view.*

class UpcomingRecyclerAdapter(private val listener: CheckClickListener) :
    ListAdapter<ModelReservationH, UpcomingRecyclerAdapter.ViewHolder>(DIFF) {

    fun getItemAtPos(position: Int): ModelReservationH {
        return getItem(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)

            val fullName =
                "${current.doctor.lastName} ${current.doctor.firstName} ${current.doctor.patronymic}"

            val pos = current.doctor.categories[0].name
//            for (c in current.doctor.categories) {
//                pos += "${c.name}\n"
//            }

            itemView.doctorNameTextViewUpcom.text = fullName
            itemView.textViewDoctorTypeRowUpcom.text = pos

            val temp = current.start.takeLast(8)
            var temp2 = temp.take(2).toInt()
            Log.d("ViewHolder", "onBind (line 39): $temp2 ")
            val date = current.start.take(10)
            var day = date.take(2).toInt()
            temp2 += 6
            if (temp2 > 23) {
                temp2 -= 24
                day += 1
            }
            val day1 = if (day >= 10)
                "$day"
            else
                "0$day"
            val temp3 = if (temp2 >= 10)
                "$temp2"
            else
                "0$temp2"
            val textToShow = "$day1.${date.takeLast(7)} $temp3:${temp.takeLast(5)}"

            itemView.textViewDateTimeUpcom.text = textToShow

            itemView.chequeButtonUpcom.setOnClickListener {
                listener.onCheckClick(current.checkUrl)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_upcoming, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelReservationH>() {
            override fun areItemsTheSame(
                oldItem: ModelReservationH,
                newItem: ModelReservationH
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ModelReservationH,
                newItem: ModelReservationH
            ): Boolean {
                return oldItem.doctor == newItem.doctor
                        && oldItem.start == newItem.start
            }

        }
    }
}