package dev.onofre.cashflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dev.onofre.cashflow.R
import dev.onofre.cashflow.databinding.ListItemTransactionBinding
import dev.onofre.cashflow.model.Transaction
import dev.onofre.cashflow.model.TransactionType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(
    private var transactions: MutableList<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    private val dateFormatter = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ListItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    fun updateList(newTransactions: List<Transaction>) {
        transactions.clear()
        transactions.addAll(newTransactions)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(private val binding: ListItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.tvItemDescription.text = transaction.description
            binding.tvItemCategory.text = transaction.category
            binding.tvItemTimestamp.text = dateFormatter.format(Date(transaction.timestamp))

            val formattedAmount = currencyFormatter.format(transaction.amount)
            val colorRes: Int
            val amountPrefix: String
            if (transaction.type == TransactionType.INCOME) {
                colorRes = R.color.income_color
                amountPrefix = "+ "
            } else {
                colorRes = R.color.expense_color
                amountPrefix = "- "
            }

            binding.tvItemAmount.text = buildString {
                append(amountPrefix)
                append(formattedAmount)
            }
            binding.tvItemAmount.setTextColor(ContextCompat.getColor(binding.root.context, colorRes))
        }
    }
}