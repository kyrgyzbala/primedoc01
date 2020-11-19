package kg.kyrgyzcoder.primedoc01.ui.clinic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.clinics.model.ModelCategoryItem
import kotlinx.android.synthetic.main.row_clinic.view.*

class CategoryRecyclerViewAdapter(private val listener: ClinicClickListener) :
    ListAdapter<ModelCategoryItem, CategoryRecyclerViewAdapter.CViewHolder>(DIFF) {

    fun getItemAtPos(position: Int): ModelCategoryItem {
        return getItem(position)
    }

    inner class CViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)

            if (!current.image.isNullOrEmpty())
                Glide.with(itemView).load(current.image).into(itemView.imgViewRowClinic)
            itemView.textViewTypeRowClinic.text = current.name
            itemView.textViewDescRowClinic.text = current.description

            itemView.setOnClickListener {
                listener.onClick(position)
            }
            itemView.imgViewRowClinic.setOnClickListener {
                listener.onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CViewHolder {
        return CViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_clinic, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CViewHolder, position: Int) {
        holder.onBind(position)
    }

    interface ClinicClickListener {
        fun onClick(position: Int)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelCategoryItem>() {
            override fun areItemsTheSame(
                oldItem: ModelCategoryItem,
                newItem: ModelCategoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ModelCategoryItem,
                newItem: ModelCategoryItem
            ): Boolean {
                return oldItem.description == newItem.description
            }

        }
    }
}