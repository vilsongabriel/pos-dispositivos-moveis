package dev.onofre.appcontatos.ui.contact.list

import dev.onofre.appcontatos.data.Contact

data class ContactsListState (
    var isLoading: Boolean = false,
    var hasError: Boolean = false,
    var contacts: Map<String, List<Contact>> = emptyMap()
)