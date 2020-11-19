package kg.kyrgyzcoder.primedoc01.ui.faq

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.faq.ModelFaqItem
import kotlinx.android.synthetic.main.row_faq.view.*

class FaqRecyclerViewAdapter : ListAdapter<ModelFaqItem, FaqRecyclerViewAdapter.FViewHolder>(DIFF) {

    inner class FViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(position: Int) {
            val current = getItem(position)

            Log.d("FaqFragment", "onBind (line 21): $current")

            itemView.questionFaqRow.text = current.question
            itemView.answerTextViewFaqRow.text = current.answer

            itemView.expandButtonFaqRow.setOnClickListener {
                itemView.answerTextViewFaqRow.visibility =
                    if (itemView.answerTextViewFaqRow.visibility == View.VISIBLE)
                        View.GONE
                    else
                        View.VISIBLE
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FViewHolder {
        return FViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_faq, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FViewHolder, position: Int) {
        holder.onBind(position)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelFaqItem>() {
            override fun areItemsTheSame(oldItem: ModelFaqItem, newItem: ModelFaqItem): Boolean {
                return oldItem.question == newItem.question
            }

            override fun areContentsTheSame(oldItem: ModelFaqItem, newItem: ModelFaqItem): Boolean {
                return oldItem.answer == newItem.answer &&
                        oldItem.question == newItem.question
            }

        }
    }

}