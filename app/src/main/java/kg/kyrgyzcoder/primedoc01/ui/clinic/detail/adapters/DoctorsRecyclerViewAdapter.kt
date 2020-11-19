package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.clinics.model.detail.Doctor
import kotlinx.android.synthetic.main.row_doctors.view.*

class DoctorsRecyclerViewAdapter(private val listener: DoctorClickListener) :
    ListAdapter<Doctor, DoctorsRecyclerViewAdapter.DViewHolder>(DIFF) {

    fun getItemAtPos(position: Int): Doctor {
        return getItem(position)
    }

    inner class DViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)

            if (!current.image.isNullOrEmpty())
                Glide.with(itemView).load(current.image).into(itemView.imgViewDocRow)

            val fullName = "${current.lastName} ${current.firstName} ${current.patronymic}"
            itemView.doctorNameTextViewRowDoc.text = fullName
            itemView.docCatRowDoc.text = current.position

            itemView.setOnClickListener {
                listener.onClick(position)
            }

            itemView.imgViewDocRow.setOnClickListener {
                listener.onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DViewHolder {
        return DViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_doctors, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DViewHolder, position: Int) {
        holder.onBind(position)
    }

    interface DoctorClickListener {
        fun onClick(position: Int)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Doctor>() {
            override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
                return oldItem.position == newItem.position &&
                        oldItem.firstName == newItem.firstName
            }

        }
    }
}