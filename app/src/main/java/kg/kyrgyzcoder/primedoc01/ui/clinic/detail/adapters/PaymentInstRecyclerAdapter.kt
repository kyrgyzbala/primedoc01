package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPaymentOptions
import kg.kyrgyzcoder.primedoc01.data.payment.PaymentStep
import kotlinx.android.synthetic.main.row_payment_instr.view.*

class PaymentInstRecyclerAdapter(private val color: String) :
    ListAdapter<PaymentStep, PaymentInstRecyclerAdapter.ViewHolder>(DIFF) {

    fun getItemAtPos(position: Int): PaymentStep {
        return getItem(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)

            itemView.numberTextViewRowPayment.text = current.number.toString()
            itemView.stepTextViewRowPayment.text = current.text
            itemView.numberCardViewRowPayment.setCardBackgroundColor(Color.parseColor(color))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_payment_instr, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<PaymentStep>() {
            override fun areItemsTheSame(oldItem: PaymentStep, newItem: PaymentStep): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PaymentStep, newItem: PaymentStep): Boolean {
                return oldItem.text == newItem.text &&
                        oldItem.number == newItem.number
            }

        }
    }

}