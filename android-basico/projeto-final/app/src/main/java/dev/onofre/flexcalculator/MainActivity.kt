package dev.onofre.flexcalculator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import kotlin.math.abs
import androidx.core.net.toUri

data class Fuel(val name: String, val consumption: Float) : java.io.Serializable

enum class FuelType(val requestCode: Int) {
    FUEL1(100),
    FUEL2(101)
}

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FUEL = "extra_fuel"
        const val COST_EPSILON = 0.001f
    }

    private var fuel1: Fuel? = null
    private var fuel2: Fuel? = null
    private var manualConsumption1: Float? = null
    private var manualConsumption2: Float? = null

    private lateinit var btnFuel1Select: MaterialButton
    private lateinit var btnFuel2Select: MaterialButton
    private lateinit var etFuel1Price: EditText
    private lateinit var etFuel2Price: EditText
    private lateinit var tvFuel1Consumption: TextView
    private lateinit var tvFuel2Consumption: TextView
    private lateinit var videoBackground: VideoView
    private var currentVideoPosition: Int = 0
    private lateinit var customFuelName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        customFuelName = getString(R.string.custom_fuel_name)
        initializeViews()
        setupListeners()
        setupVideoBackground()
    }

    private fun initializeViews() {
        btnFuel1Select = findViewById(R.id.btnFuel1Select)
        btnFuel2Select = findViewById(R.id.btnFuel2Select)
        tvFuel1Consumption = findViewById(R.id.tvFuel1Consumption)
        tvFuel2Consumption = findViewById(R.id.tvFuel2Consumption)
        etFuel1Price = findViewById(R.id.etFuel1Price)
        etFuel2Price = findViewById(R.id.etFuel2Price)
        etFuel1Price.addCurrencyMask()
        etFuel2Price.addCurrencyMask()
        videoBackground = findViewById(R.id.videoBackground)
    }

    private fun setupListeners() {
        val containerFuel1Consumption = findViewById<LinearLayout>(R.id.containerFuel1Consumption)
        val containerFuel2Consumption = findViewById<LinearLayout>(R.id.containerFuel2Consumption)
        val btnCalculate = findViewById<MaterialButton>(R.id.btnCalculate)
        containerFuel1Consumption.setOnClickListener { showManualConsumptionDialog(FuelType.FUEL1) }
        containerFuel2Consumption.setOnClickListener { showManualConsumptionDialog(FuelType.FUEL2) }
        btnFuel1Select.setOnClickListener { startFuelSelection(FuelType.FUEL1) }
        btnFuel2Select.setOnClickListener { startFuelSelection(FuelType.FUEL2) }
        btnCalculate.setOnClickListener { onCalculateClicked() }
    }

    private fun setupVideoBackground() {
        val videoPath = "android.resource://${packageName}/${R.raw.background}"
        videoBackground.setVideoURI(videoPath.toUri())
        videoBackground.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            videoBackground.post {
                val width = videoBackground.width
                val height = width * 9 / 16
                videoBackground.layoutParams.height = height
                videoBackground.requestLayout()
            }
        }
        videoBackground.start()
    }

    private fun startFuelSelection(fuelType: FuelType) {
        val intent = Intent(this, FuelSelectionActivity::class.java)
        startActivityForResult(intent, fuelType.requestCode)
    }

    override fun onPause() {
        super.onPause()
        currentVideoPosition = videoBackground.currentPosition
        videoBackground.pause()
    }

    override fun onResume() {
        super.onResume()
        videoBackground.seekTo(currentVideoPosition)
        videoBackground.start()
    }

    private fun onCalculateClicked() {
        if (fuel1 == null || fuel2 == null) {
            Toast.makeText(this, getString(R.string.msg_select_two_fuels), Toast.LENGTH_SHORT).show()
            return
        }
        val price1 = etFuel1Price.text.toString().extractCurrencyValue()
        val price2 = etFuel2Price.text.toString().extractCurrencyValue()
        if (price1 <= 0 || price2 <= 0) {
            Toast.makeText(this, getString(R.string.msg_enter_valid_prices), Toast.LENGTH_SHORT).show()
            return
        }
        val consumption1 = getEffectiveConsumption(fuel1, manualConsumption1)
        val consumption2 = getEffectiveConsumption(fuel2, manualConsumption2)
        val cost1 = price1 / consumption1
        val cost2 = price2 / consumption2
        val resultFuel = when {
            abs(cost1 - cost2) < COST_EPSILON -> ""
            cost1 < cost2 -> fuel1!!.name
            else -> fuel2!!.name
        }
        val resultIntent = Intent(this, ResultActivity::class.java).apply {
            putExtra("result", resultFuel)
            putExtra("cost1", cost1)
            putExtra("cost2", cost2)
            putExtra("fuel1Name", fuel1!!.name)
            putExtra("fuel2Name", fuel2!!.name)
        }
        startActivity(resultIntent)
    }

    private fun getEffectiveConsumption(fuel: Fuel?, manualConsumption: Float?): Float {
        if (fuel == null) throw IllegalArgumentException(getString(R.string.error_fuel_null))
        return if (fuel.name.equals(customFuelName, ignoreCase = true) || manualConsumption != null) {
            manualConsumption ?: fuel.consumption
        } else {
            fuel.consumption
        }
    }

    private fun showManualConsumptionDialog(fuelType: FuelType) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.title_set_consumption))
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        input.addConsumptionMask()
        builder.setView(input)
        builder.setPositiveButton(getString(R.string.button_ok)) { _, _ ->
            val value = input.text.toString().toFloatOrNull()
            if (value == null || value <= 0) {
                Toast.makeText(this, getString(R.string.msg_invalid_consumption), Toast.LENGTH_SHORT).show()
            } else {
                when (fuelType) {
                    FuelType.FUEL1 -> {
                        manualConsumption1 = value
                        if (fuel1 == null) {
                            fuel1 = Fuel(customFuelName, value)
                            btnFuel1Select.text = customFuelName
                        }
                        tvFuel1Consumption.text = getString(R.string.label_consumption_value, value)
                    }
                    FuelType.FUEL2 -> {
                        manualConsumption2 = value
                        if (fuel2 == null) {
                            fuel2 = Fuel(customFuelName, value)
                            btnFuel2Select.text = customFuelName
                        }
                        tvFuel2Consumption.text = getString(R.string.label_consumption_value, value)
                    }
                }
            }
        }
        builder.setNegativeButton(getString(R.string.button_cancel)) { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun updateFuelSelection(fuelType: FuelType, selectedFuel: Fuel) {
        when (fuelType) {
            FuelType.FUEL1 -> {
                fuel1 = selectedFuel
                btnFuel1Select.text = selectedFuel.name
                if (selectedFuel.name.equals(customFuelName, ignoreCase = true)) {
                    showManualConsumptionDialog(FuelType.FUEL1)
                } else {
                    manualConsumption1 = null
                    tvFuel1Consumption.text = getString(R.string.label_consumption_value, selectedFuel.consumption)
                }
            }
            FuelType.FUEL2 -> {
                fuel2 = selectedFuel
                btnFuel2Select.text = selectedFuel.name
                if (selectedFuel.name.equals(customFuelName, ignoreCase = true)) {
                    showManualConsumptionDialog(FuelType.FUEL2)
                } else {
                    manualConsumption2 = null
                    tvFuel2Consumption.text = getString(R.string.label_consumption_value, selectedFuel.consumption)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedFuel = data.getSerializableExtra(EXTRA_FUEL) as? Fuel ?: return
            when (requestCode) {
                FuelType.FUEL1.requestCode -> updateFuelSelection(FuelType.FUEL1, selectedFuel)
                FuelType.FUEL2.requestCode -> updateFuelSelection(FuelType.FUEL2, selectedFuel)
            }
        }
    }
}