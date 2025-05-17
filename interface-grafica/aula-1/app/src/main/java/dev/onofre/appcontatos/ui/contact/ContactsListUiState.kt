package dev.onofre.appcontatos.ui.contact

import dev.onofre.appcontatos.data.Contact

data class ContactsListUiState (
    var isLoading: Boolean = false,
    var hasError: Boolean = false,
    var contacts: Map<String, List<Contact>> = emptyMap()
)