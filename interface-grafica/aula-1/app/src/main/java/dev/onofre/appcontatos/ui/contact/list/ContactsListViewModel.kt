package dev.onofre.appcontatos.ui.contact.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.onofre.appcontatos.data.Contact
import dev.onofre.appcontatos.data.ContactDatasource
import dev.onofre.appcontatos.data.groupByInitial
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ContactsListViewModel : ViewModel() {
    var state by mutableStateOf(ContactsListState())
        private set

    private val datasource = ContactDatasource.instance

    init {
        loadContacts()
    }

    fun loadContacts() {
        state = state.copy(
            isLoading = true,
            hasError = false
        )
        val fakeLoaderDelayMs: Long = 2000

        viewModelScope.launch {
            delay(fakeLoaderDelayMs)

            state = state.copy(
                contacts = datasource.findAll().groupByInitial(),
                isLoading = false
            )
        }
    }

    fun toggleFavorite (pressedContact: Contact) {
        val updated = state.contacts.mapValues { (_, list) ->
            list.map { contact ->
                if (contact.id == pressedContact.id)
                    contact.copy(isFavorite = !contact.isFavorite)
                else
                    contact
            }
        }

        state = state.copy(contacts = updated)
    }
}