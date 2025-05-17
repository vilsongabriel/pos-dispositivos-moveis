package dev.onofre.appcontatos.ui.contact.details.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.onofre.appcontatos.R
import dev.onofre.appcontatos.data.Contact
import dev.onofre.appcontatos.ui.contact.composables.ContactAvatar
import dev.onofre.appcontatos.ui.theme.AppContatosTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ContactDetails(
    modifier: Modifier = Modifier,
    contact: Contact,
    enabled: Boolean = true,
    onContactInfoPressed: () -> Unit
) {
    Column (
        modifier = modifier
            .padding(top = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContactAvatar(
            firstName = contact.firstName,
            lastName = contact.lastName,
            size = 150.dp,
            textStyle = MaterialTheme.typography.displayLarge
        )
        Spacer(Modifier.size(24.dp))
        Text(
            text = contact.fullName,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.size(16.dp))
        QuickActionsRow(
            contact = contact,
            enabled = enabled,
        )
        CardContactInfo(
            contact = contact,
            enabled = enabled,
            onContactInfoPressed = onContactInfoPressed
        )
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        Text(
            text = stringResource(R.string.contact_details_created_at_label, formatDate(contact.createdAt)),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

private fun formatDate(date: LocalDateTime): String {
    return date.format(
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    )
}

@Preview(showBackground = true)
@Composable
private fun ContactDetailsPreview() {
    AppContatosTheme {
        ContactDetails(
            contact = Contact(
                firstName = "João",
                lastName = "Guilherme",
                phoneNumber = "(11) 98888-8888"
            ),
            onContactInfoPressed = {}
        )
    }
}