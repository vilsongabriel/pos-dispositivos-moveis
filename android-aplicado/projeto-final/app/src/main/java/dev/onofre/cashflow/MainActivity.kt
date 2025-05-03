package dev.onofre.cashflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dev.onofre.cashflow.data.dao.CategoryDAO
import dev.onofre.cashflow.data.dao.TransactionDAO
import dev.onofre.cashflow.data.db.DbHelper
import dev.onofre.cashflow.data.repository.TransactionRepository
import dev.onofre.cashflow.databinding.ActivityMainBinding
import dev.onofre.cashflow.model.Transaction
import dev.onofre.cashflow.model.TransactionType
import dev.onofre.cashflow.adapter.TransactionAdapter
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: TransactionRepository
    private lateinit var transactionAdapter: TransactionAdapter

    private val createTransactionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                handleTransactionResult(data)
            } else {
                Toast.makeText(this,
                    getString(R.string.toast_error_fetch_transaction), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DbHelper(applicationContext)
        val transactionDao = TransactionDAO(dbHelper)
        val categoryDao = CategoryDAO(dbHelper)
        repository = TransactionRepository(transactionDao, categoryDao)

        setupRecyclerView()
        setupListeners()
        loadInitialData()
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(mutableListOf())
        binding.rvMainTransactions.layoutManager = LinearLayoutManager(this)
        binding.rvMainTransactions.adapter = transactionAdapter
    }

    private fun setupListeners() {
        binding.fabMainAdd.setOnClickListener {
            handleCreateClick()
        }

        binding.etMainSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                refreshTransactionList(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun handleCreateClick() {
        val intent = Intent(this, TransactionCreateActivity::class.java)
        createTransactionLauncher.launch(intent)
    }

    private fun handleTransactionResult(data: Intent) {
        val description = data.getStringExtra(TransactionCreateActivity.EXTRA_DESCRIPTION)
        val amount = data.getDoubleExtra(TransactionCreateActivity.EXTRA_AMOUNT, 0.0)
        val typeString = data.getStringExtra(TransactionCreateActivity.EXTRA_TYPE)
        val category = data.getStringExtra(TransactionCreateActivity.EXTRA_CATEGORY)
        val timestamp = data.getLongExtra(TransactionCreateActivity.EXTRA_TIMESTAMP, System.currentTimeMillis())

        if (description != null && typeString != null && category != null && amount > 0) {
            val type = TransactionType.valueOf(typeString)
            val newTransaction = Transaction(
                description = description, amount = amount, type = type, category = category, timestamp = timestamp
            )

            lifecycleScope.launch {
                try {
                    val addedId = repository.addTransaction(newTransaction)
                    if (addedId > 0) {
                        Toast.makeText(this@MainActivity,
                            getString(R.string.toast_transaction_saved), Toast.LENGTH_SHORT).show()
                        binding.etMainSearch.text?.clear()
                        refreshTransactionList()
                        refreshCurrentBalance()
                    } else {
                        Toast.makeText(this@MainActivity,
                            getString(R.string.toast_error_save_transaction), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "${R.string.toast_error_save_transaction}: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

        } else {
            Toast.makeText(this,
                getString(R.string.toast_error_process_transaction), Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadInitialData() {
        refreshTransactionList()
        refreshCurrentBalance()
    }

    private fun refreshTransactionList(searchQuery: String? = null) {
        lifecycleScope.launch {
            try {
                val transactions = if (searchQuery.isNullOrBlank()) {
                    repository.getAllTransactions()
                } else {
                    repository.getAllTransactions().filter {
                        it.description.contains(searchQuery, ignoreCase = true)
                    }
                }
                transactionAdapter.updateList(transactions)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity,
                    getString(R.string.toast_error_load_transaction_list, e.message), Toast.LENGTH_LONG).show()
                transactionAdapter.updateList(emptyList())
            }
        }
    }

    private fun refreshCurrentBalance() {
        lifecycleScope.launch {
            try {
                val balance = repository.getCurrentBalance()
                binding.tvMainBalanceValue.text = formatCurrency(balance)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity,
                    getString(R.string.toast_error_fetch_balance, e.message), Toast.LENGTH_LONG).show()
                binding.tvMainBalanceValue.text = formatCurrency(0.0)
            }
        }
    }

    private fun formatCurrency(value: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return format.format(value)
    }
}