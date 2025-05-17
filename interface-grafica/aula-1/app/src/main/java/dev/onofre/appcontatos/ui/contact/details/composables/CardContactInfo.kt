package dev.onofre.appcontatos.ui.contact.details.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.onofre.appcontatos.R
import dev.onofre.appcontatos.data.Contact
import dev.onofre.appcontatos.ui.theme.AppContatosTheme

@Composable
fun CardContactInfo(
    modifier: Modifier = Modifier,
    contact: Contact,
    enabled: Boolean = true,
    onContactInfoPressed: () -> Unit
) {
    Card (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.contact_info_label),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        ContactInfo(
            imageVector = Icons.Outlined.Phone,
            value = contact.phoneNumber.ifBlank {
                stringResource(R.string.contact_info_add_phone)
            },
            enabled = enabled && contact.phoneNumber.isBlank(),
            onPressed = {}
        )
        ContactInfo(
            imageVector = Icons.Outlined.Mail,
            value = contact.phoneNumber.ifBlank {
                stringResource(R.string.contact_info_add_email)
            },
            enabled = enabled && contact.email.isBlank(),
            onPressed = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardContactInfoPreview() {
    AppContatosTheme {
        CardContactInfo(
            contact = Contact(),
            onContactInfoPressed = {}
        )
    }
}