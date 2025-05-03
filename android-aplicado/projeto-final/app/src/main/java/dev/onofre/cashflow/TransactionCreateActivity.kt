package dev.onofre.cashflow

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.onofre.cashflow.data.dao.CategoryDAO
import dev.onofre.cashflow.data.dao.TransactionDAO
import dev.onofre.cashflow.data.db.DbHelper
import dev.onofre.cashflow.data.repository.TransactionRepository
import dev.onofre.cashflow.databinding.ActivityTransactionCreateBinding
import dev.onofre.cashflow.model.TransactionType
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransactionCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionCreateBinding
    private lateinit var categoryAdapter: ArrayAdapter<String>
    private lateinit var repository: TransactionRepository
    private val selectedDateTime = Calendar.getInstance()
    private val dateTimeFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    companion object {
        const val EXTRA_DESCRIPTION = "dev.onofre.cashflow.EXTRA_DESCRIPTION"
        const val EXTRA_AMOUNT = "dev.onofre.cashflow.EXTRA_AMOUNT"
        const val EXTRA_TYPE = "dev.onofre.cashflow.EXTRA_TYPE"
        const val EXTRA_CATEGORY = "dev.onofre.cashflow.EXTRA_CATEGORY"
        const val EXTRA_TIMESTAMP = "dev.onofre.cashflow.EXTRA_TIMESTAMP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DbHelper(applicationContext)
        val transactionDao = TransactionDAO(dbHelper)
        val categoryDao = CategoryDAO(dbHelper)
        repository = TransactionRepository(transactionDao, categoryDao)

        setupCategorySpinner()
        setupRadioGroupListener()
        setupDateTimePicker()
        setupSaveButtonListener()

        updateDateTimeDisplay()
        updateCategoriesForType(TransactionType.INCOME)
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(Activity.RESULT_CANCELED)
        finish()
        return true
    }

    private fun setupCategorySpinner() {
        categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf<String>())
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCreateCategory.adapter = categoryAdapter
        binding.spinnerCreateCategory.visibility = View.GONE
    }

    private fun setupRadioGroupListener() {
        binding.rgCreateType.setOnCheckedChangeListener { _, checkedId ->
            val selectedType = when (checkedId) {
                R.id.rb_create_income -> TransactionType.INCOME
                R.id.rb_create_expense -> TransactionType.EXPENSE
                else -> null
            }
            updateCategoriesForType(selectedType)
        }
    }

    private fun updateCategoriesForType(type: TransactionType?) {
        if (type == null) {
            categoryAdapter.clear()
            binding.spinnerCreateCategory.visibility = View.GONE
            categoryAdapter.notifyDataSetChanged()
            return
        }

        lifecycleScope.launch {
            try {
                val categories = repository.getCategoriesByType(type)
                val categoryNames = categories.map { it.name }

                categoryAdapter.clear()
                if (categoryNames.isNotEmpty()) {
                    categoryAdapter.addAll(categoryNames)
                    binding.spinnerCreateCategory.visibility = View.VISIBLE
                } else {
                    binding.spinnerCreateCategory.visibility = View.GONE
                }
                categoryAdapter.notifyDataSetChanged()

            } catch (_: Exception) {
                Toast.makeText(this@TransactionCreateActivity,
                    getString(R.string.toast_fetch_error), Toast.LENGTH_SHORT).show()
                categoryAdapter.clear()
                binding.spinnerCreateCategory.visibility = View.GONE
                categoryAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupDateTimePicker() {
        binding.tvCreateDatetimeValue.setOnClickListener {
            showDatePicker()
        }
    }

    private fun updateDateTimeDisplay() {
        binding.tvCreateDatetimeValue.text = dateTimeFormatter.format(selectedDateTime.time)
    }

    private fun showDatePicker() {
        val year = selectedDateTime.get(Calendar.YEAR)
        val month = selectedDateTime.get(Calendar.MONTH)
        val day = selectedDateTime.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            selectedDateTime.set(Calendar.YEAR, selectedYear)
            selectedDateTime.set(Calendar.MONTH, selectedMonth)
            selectedDateTime.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)
            showTimePicker()
        }, year, month, day).show()
    }

    private fun showTimePicker() {
        val hour = selectedDateTime.get(Calendar.HOUR_OF_DAY)
        val minute = selectedDateTime.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            selectedDateTime.set(Calendar.HOUR_OF_DAY, selectedHour)
            selectedDateTime.set(Calendar.MINUTE, selectedMinute)
            selectedDateTime.set(Calendar.SECOND, 0)
            selectedDateTime.set(Calendar.MILLISECOND, 0)
            updateDateTimeDisplay()
        }, hour, minute, true).show()
    }


    private fun setupSaveButtonListener() {
        binding.btnCreateSave.setOnClickListener {
            saveTransaction()
        }
    }

    private fun saveTransaction() {
        val description = binding.etCreateDescription.text.toString().trim()
        val amountStr = binding.etCreateAmount.text.toString().trim()
        val selectedTypeId = binding.rgCreateType.checkedRadioButtonId
        val selectedCategory = binding.spinnerCreateCategory.selectedItem as? String
        val timestamp = selectedDateTime.timeInMillis

        if (description.isBlank()) {
            Toast.makeText(this,
                getString(R.string.toast_error_fill_description), Toast.LENGTH_SHORT).show()
            binding.etCreateDescription.error = getString(R.string.required)
            return
        } else {
            binding.etCreateDescription.error = null
        }

        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, getString(R.string.toast_error_invalid_value), Toast.LENGTH_SHORT).show()
            binding.etCreateAmount.error = getString(R.string.invalid_value)
            return
        } else {
            binding.etCreateAmount.error = null
        }


        val type: TransactionType? = when (selectedTypeId) {
            R.id.rb_create_income -> TransactionType.INCOME
            R.id.rb_create_expense -> TransactionType.EXPENSE
            else -> null
        }

        if (type == null) {
            Toast.makeText(this, getString(R.string.toast_error_invalid_type), Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.spinnerCreateCategory.visibility != View.VISIBLE || selectedCategory.isNullOrBlank()) {
            Toast.makeText(this, getString(R.string.toast_error_select_category), Toast.LENGTH_SHORT).show()
            return
        }

        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_DESCRIPTION, description)
        resultIntent.putExtra(EXTRA_AMOUNT, amount)
        resultIntent.putExtra(EXTRA_TYPE, type.name)
        resultIntent.putExtra(EXTRA_CATEGORY, selectedCategory)
        resultIntent.putExtra(EXTRA_TIMESTAMP, timestamp)

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}