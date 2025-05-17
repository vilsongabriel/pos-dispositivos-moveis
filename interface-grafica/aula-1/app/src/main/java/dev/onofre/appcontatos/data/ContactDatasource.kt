package dev.onofre.appcontatos.data

import kotlin.random.Random

class ContactDatasource private constructor() {
    companion object {
        val instance: ContactDatasource by lazy {
            ContactDatasource()
        }
    }

    private val contacts: MutableList<Contact> = mutableListOf()

    init {
        contacts.addAll(generateMockContactList())
    }

    fun findAll(): List<Contact> {
        return contacts.toList()
    }

    fun findById(id: Int): Contact? =
        contacts.find { contact -> contact.id == id }


    fun save(contact: Contact): Contact =
        if (contact.id > 0) update(contact) else insert(contact)

    fun delete(id: Int) {
        contacts.removeIf { it.id == id }
    }

    private fun nextId(): Int = (contacts.maxOfOrNull { it.id } ?: 0) + 1

    private fun insert(contact: Contact): Contact =
        contact.copy(id = nextId()).also(contacts::add)

    private fun update(contact: Contact): Contact {
        val index = contacts.indexOfFirst { it.id == contact.id }
        contacts[index] = contact
        return contact
    }
}

fun generateMockContactList(): List<Contact> {
    val firstNames = listOf(
        "João", "José", "Everton", "Marcos", "André", "Anderson", "Antônio",
        "Laura", "Ana", "Maria", "Joaquina", "Suelen", "Samuel"
    )
    val lastNames = listOf(
        "Do Carmo", "Oliveira", "Dos Santos", "Da Silva", "Brasil", "Pichetti",
        "Cordeiro", "Silveira", "Andrades", "Cardoso", "Souza"
    )
    val contacts: MutableList<Contact> = mutableListOf()
    for (i in 0..19) {
        var generatedNewContact = false
        while (!generatedNewContact) {
            val firstNameIndex = Random.nextInt(firstNames.size)
            val lastNameIndex = Random.nextInt(lastNames.size)
            val newContact = Contact(
                id = i + 1,
                firstName = firstNames[firstNameIndex],
                lastName = lastNames[lastNameIndex]
            )
            if (!contacts.any { it.fullName == newContact.fullName }) {
                contacts.add(newContact)
                generatedNewContact = true
            }
        }
    }
    return contacts
}