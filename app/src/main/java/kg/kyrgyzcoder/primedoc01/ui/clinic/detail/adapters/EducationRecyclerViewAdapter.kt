package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.clinics.model.doctor.Information
import kotlinx.android.synthetic.main.row_edu.view.*
import kotlin.math.E

class EducationRecyclerViewAdapter :
    ListAdapter<Information, EducationRecyclerViewAdapter.EViewHolder>(DIFF) {

    fun getItemAtPos(position: Int): Information {
        return getItem(position)
    }

    inner class EViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)

            Log.d("EViewHolder", "onBind (line 27): current $current")

            itemView.yearRowEdu.text = current.start?.take(4)
            val text = if (current.name == null)
                current.organizationName
            else
                "${current.name},\n ${current.organizationName}"
            itemView.descEduRow.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EViewHolder {
        return EViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_edu, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EViewHolder, position: Int) {
        holder.onBind(position)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Information>() {
            override fun areItemsTheSame(oldItem: Information, newItem: Information): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Information, newItem: Information): Boolean {
                return oldItem.organizationName == newItem.organizationName &&
                        oldItem.start == newItem.start
            }

        }
    }
}