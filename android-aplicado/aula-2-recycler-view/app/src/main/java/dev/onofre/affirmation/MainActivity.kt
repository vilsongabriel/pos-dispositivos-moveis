package dev.onofre.affirmation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import dev.onofre.affirmation.adapter.ItemAdapter
import dev.onofre.affirmation.data.Datasource

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val dataset = Datasource().loadAffirmation()
        val adapter = ItemAdapter(this, dataset)

        recyclerView.adapter = adapter
    }
}