package dev.onofre.flexcalculator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import kotlin.math.abs

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val tvResult = findViewById<TextView>(R.id.tvResult)
        val btnBack = findViewById<MaterialButton>(R.id.btnBack)

        val resultFuel = intent.getStringExtra("result") ?: ""
        val cost1 = intent.getFloatExtra("cost1", 0f)
        val cost2 = intent.getFloatExtra("cost2", 0f)
        val fuel1Name = intent.getStringExtra("fuel1Name") ?: getString(R.string.fuel_1)
        val fuel2Name = intent.getStringExtra("fuel2Name") ?: getString(R.string.fuel_2)

        if (abs(cost1 - cost2) < 0.001f) {
            tvResult.text = getString(R.string.fuel_equality,
                fuel1Name, fuel2Name, cost1, cost2)
        } else {
            tvResult.text = getString(R.string.fuel_advantage,
                resultFuel, fuel1Name, cost1, fuel2Name, cost2)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}