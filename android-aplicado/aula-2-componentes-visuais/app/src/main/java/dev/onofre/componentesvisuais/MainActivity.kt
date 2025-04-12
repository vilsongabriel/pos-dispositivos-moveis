package dev.onofre.componentesvisuais

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var screen: LinearLayout
    private lateinit var checkbox: CheckBox
    private lateinit var rating: RatingBar
    private lateinit var datepicker: DatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screen = findViewById(R.id.main)
        checkbox = findViewById(R.id.checkbox)
        rating = findViewById(R.id.rating_bar)
        datepicker = findViewById(R.id.datepicker)
    }

    private fun showMessage(message: String) {
        Snackbar.make(screen, message, Snackbar.LENGTH_SHORT).apply {
            setAction("Cancelar") {
                this.dismiss()
            }
        }.show()
    }

    fun handleButtonTestOnClick(view: android.view.View) {
        val message = StringBuilder()

        message.append(if (checkbox.isChecked) "Selecionado." else "Não selecionado.")
        message.append(" Avaliação: ${rating.rating}")

        val formattedDate = String.format("%02d/%02d/%d", datepicker.dayOfMonth, datepicker.month + 1, datepicker.year)
        message.append(" Data: $formattedDate")

        showMessage(message.toString())
    }

    fun handleButtonCalendarOnClick(view: android.view.View) {
        DatePickerDialog(this,
            { view, year, month, day ->
                val formattedDate = String.format("%02d/%02d/%d", day, month + 1, year)
                showMessage(formattedDate)
            },
            datepicker.year,
            datepicker.month,
            datepicker.dayOfMonth
        ).show()
    }
}