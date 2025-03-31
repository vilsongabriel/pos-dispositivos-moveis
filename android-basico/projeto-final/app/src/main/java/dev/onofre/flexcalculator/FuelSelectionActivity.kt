package dev.onofre.flexcalculator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class FuelSelectionActivity : AppCompatActivity() {

    private lateinit var fuels: List<Fuel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fuel_selection)

        fuels = listOf(
            Fuel(getString(R.string.fuel_gasoline), 10f),
            Fuel(getString(R.string.fuel_ethanol), 8f),
            Fuel(getString(R.string.fuel_diesel), 12f),
            Fuel(getString(R.string.fuel_custom), 0f)
        )

        val lvFuel = findViewById<ListView>(R.id.lvFuel)
        val adapter = ArrayAdapter(this, R.layout.list_item_white, fuels.map { it.name })
        lvFuel.adapter = adapter

        lvFuel.setOnItemClickListener { _, _, position, _ ->
            val selectedFuel = fuels[position]
            val resultIntent = Intent().apply {
                putExtra(MainActivity.EXTRA_FUEL, selectedFuel)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}