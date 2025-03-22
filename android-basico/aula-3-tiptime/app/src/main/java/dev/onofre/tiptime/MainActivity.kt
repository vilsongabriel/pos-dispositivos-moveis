package dev.onofre.tiptime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.onofre.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener {
            calculateTip()
        }

        setTip(0.0)

        if (savedInstanceState != null) {
            binding.tipResult.text = savedInstanceState.getString("result")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("result", binding.tipResult.text.toString())
    }

    private fun calculateTip() {
        val stringInTextField = binding.costOfService.text.toString()
        val cost = stringInTextField.toDoubleOrNull() ?: return
        val selectedId = binding.tipOption.checkedRadioButtonId
        val tipPercentage = when (selectedId) {
            R.id.option_twenty_percent -> 0.2
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
        var tip = cost * tipPercentage

        val roundUp = binding.roundUpSwitch.isChecked
        if (roundUp) {
            tip = ceil(tip)
        }

        setTip(tip)
    }

    private fun setTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip.toString())
    }
}