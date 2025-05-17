package dev.onofre.appcontatos.ui.contact.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.onofre.appcontatos.data.ContactDatasource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ContactDetailsViewModel : ViewModel() {
    var state: ContactDetailsState by mutableStateOf(ContactDetailsState())
        private set

    private val datasource = ContactDatasource.instance

    init {
        loadContact()
    }

    fun loadContact() {
        state = state.copy(
            isLoading = true,
            hasErrorLoading = false,
        )
        viewModelScope.launch {
            delay(2000)
            val hasError = Random.nextBoolean()
            val contact = datasource.findById(1)
            state = if (hasError || contact == null) {
                state.copy(
                    isLoading = false,
                    hasErrorLoading = true
                )
            } else {
                state.copy(
                    isLoading = false,
                    contact = contact
                )
            }
        }
    }
}