package dev.onofre.flexcalculator

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.Locale

fun EditText.addCurrencyMask() {
    this.addTextChangedListener(object : TextWatcher {
        private var currentText = ""
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (s.toString() != currentText) {
                this@addCurrencyMask.removeTextChangedListener(this)
                val cleanString = s.toString().replace("[.,\\s]".toRegex(), "")
                val parsed = if (cleanString.isEmpty()) 0.0 else cleanString.toDouble()
                val formatted = NumberFormat.getNumberInstance(Locale("pt", "BR")).apply {
                    minimumFractionDigits = 2
                    maximumFractionDigits = 2
                }.format(parsed / 100)
                currentText = formatted
                this@addCurrencyMask.setText(formatted)
                this@addCurrencyMask.setSelection(formatted.length)
                this@addCurrencyMask.addTextChangedListener(this)
            }
        }
    })
}

fun String.extractCurrencyValue(): Float {
    val cleanString = this.replace("[.\\s]".toRegex(), "").replace(",", ".")
    return cleanString.toFloatOrNull() ?: 0f
}

fun EditText.addConsumptionMask() {
    this.addTextChangedListener(object : TextWatcher {
        private var currentText = ""
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            val inputText = s.toString()
            if (inputText != currentText) {
                this@addConsumptionMask.removeTextChangedListener(this)
                val cleanString = inputText.replace("[^\\d]".toRegex(), "")
                val rawValue = if (cleanString.isEmpty()) 0.0 else cleanString.toDouble()
                val consumptionValue = rawValue / 10.0
                val formatted = String.format(Locale.US, "%.1f", consumptionValue)
                currentText = formatted
                this@addConsumptionMask.setText(formatted)
                this@addConsumptionMask.setSelection(formatted.length)
                this@addConsumptionMask.addTextChangedListener(this)
            }
        }
    })
}