package com.example.myappbudget.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myappbudget.R
import com.example.myappbudget.data.database.TransactionEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(private val onItemClicked: (TransactionEntity) -> Unit) :
    ListAdapter<TransactionEntity, TransactionAdapter.TransactionViewHolder>(DiffCallback()) {

    var currencySymbol: String = "Kč"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, currencySymbol)
        holder.itemView.setOnClickListener { onItemClicked(item) }
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate) // <-- Nové

        fun bind(transaction: TransactionEntity, currencySymbol: String) {
            tvTitle.text = transaction.title
            tvCategory.text = transaction.category

            // --- ZOBRAZENÍ DATA ---
            val dateFormat = SimpleDateFormat("d. M. yyyy", Locale.getDefault())
            tvDate.text = dateFormat.format(Date(transaction.date))

            val iconRes = when (transaction.category) {
                "Jídlo" -> R.drawable.ic_food
                "Bydlení" -> R.drawable.ic_housing
                "Doprava" -> R.drawable.ic_transport
                "Zábava" -> R.drawable.ic_entertainment
                "Nákupy" -> R.drawable.ic_shopping
                "Výplata" -> R.drawable.ic_salary
                else -> R.drawable.ic_other
            }
            ivIcon.setImageResource(iconRes)

            if (transaction.type == "INCOME") {
                ivIcon.setColorFilter(Color.parseColor("#4CAF50"))
                tvAmount.text = "+ ${transaction.amount} $currencySymbol"
                tvAmount.setTextColor(Color.parseColor("#4CAF50"))
            } else {
                ivIcon.setColorFilter(Color.parseColor("#555555"))
                tvAmount.text = "- ${transaction.amount} $currencySymbol"
                tvAmount.setTextColor(Color.parseColor("#F44336"))
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TransactionEntity>() {
        override fun areItemsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity) = oldItem == newItem
    }
}