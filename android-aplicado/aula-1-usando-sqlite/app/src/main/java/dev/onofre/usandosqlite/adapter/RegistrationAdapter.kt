package dev.onofre.usandosqlite.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import dev.onofre.usandosqlite.R
import dev.onofre.usandosqlite.RegistrationActivity
import dev.onofre.usandosqlite.entity.Registration

class RegistrationAdapter(var context: Context, var registrations: List<Registration>) : BaseAdapter() {
    override fun getCount(): Int {
        return registrations.count()
    }

    override fun getItem(position: Int): Any {
        return registrations[position]
    }

    override fun getItemId(position: Int): Long {
        return registrations[position].id.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.registration_list_item, parent, false)
        val registration = registrations[position]

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhone)
        val btEdit = view.findViewById<ImageButton>(R.id.btEdit)

        tvName.text = registration.name
        tvPhone.text = registration.phone

        btEdit.setOnClickListener {
            val intent = Intent(context, RegistrationActivity::class.java).apply {
                putExtra("code", registration.id)
                putExtra("name", registration.name)
                putExtra("phone", registration.phone)
            }
            context.startActivity(intent)
        }

        return view
    }
}