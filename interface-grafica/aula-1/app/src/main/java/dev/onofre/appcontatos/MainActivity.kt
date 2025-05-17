package dev.onofre.appcontatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.onofre.appcontatos.ui.contact.list.ContactsListScreen
import dev.onofre.appcontatos.ui.theme.AppContatosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppContatosTheme {
                ContactsListScreen()
            }
        }
    }
}