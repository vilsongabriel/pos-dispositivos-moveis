package dev.onofre.appcontatos.ui.contact.details

import dev.onofre.appcontatos.data.Contact

data class ContactDetailsState (
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val contact: Contact = Contact()
)