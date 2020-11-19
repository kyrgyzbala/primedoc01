package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPaymentOptions
import kotlinx.android.synthetic.main.row_payment_options.view.*

class PaymentOptionsRecyclerAdapter(private val listener: PaymentOptionsClickListener) :
    ListAdapter<ModelPaymentOptions, PaymentOptionsRecyclerAdapter.ViewHolder>(DIFF) {

    fun getItemAtPos(position: Int): ModelPaymentOptions {
        return getItem(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {

            val current = getItemAtPos(position)

            if (!current.logo.isNullOrEmpty()) {
                Glide.with(itemView).load(current.logo).into(itemView.imgViewRowPaymentOptions)
            }

            itemView.imgViewRowPaymentOptions.setOnClickListener {
                listener.onClick(position)
            }

            itemView.setOnClickListener {
                listener.onClick(position)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_payment_options, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    interface PaymentOptionsClickListener {
        fun onClick(position: Int)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelPaymentOptions>() {
            override fun areItemsTheSame(
                oldItem: ModelPaymentOptions,
                newItem: ModelPaymentOptions
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ModelPaymentOptions,
                newItem: ModelPaymentOptions
            ): Boolean {
                return oldItem.name == newItem.name
            }

        }
    }

}