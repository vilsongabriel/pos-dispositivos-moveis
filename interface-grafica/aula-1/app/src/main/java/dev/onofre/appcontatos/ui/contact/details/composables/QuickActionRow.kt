package dev.onofre.appcontatos.ui.contact.details.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.onofre.appcontatos.R
import dev.onofre.appcontatos.data.Contact
import dev.onofre.appcontatos.ui.theme.AppContatosTheme

@Composable
fun QuickActionsRow(
    modifier: Modifier = Modifier,
    contact: Contact,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        QuickAction(
            imageVector = Icons.Filled.Phone,
            text = stringResource(R.string.quick_action_call_label),
            onPressed = {},
            enabled = enabled && contact.phoneNumber.isNotBlank()
        )
        QuickAction(
            imageVector = Icons.Filled.Sms,
            text = stringResource(R.string.quick_action_sms_label),
            onPressed = {},
            enabled = enabled && contact.phoneNumber.isNotBlank()
        )
        QuickAction(
            imageVector = Icons.Filled.Videocam,
            text = stringResource(R.string.quick_action_video_label),
            onPressed = {},
            enabled = enabled && contact.phoneNumber.isNotBlank()
        )
        QuickAction(
            imageVector = Icons.Filled.Email,
            text = stringResource(R.string.quick_action_email_label),
            onPressed = {},
            enabled = enabled && contact.email.isNotBlank()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuickActionsRowPreview() {
    AppContatosTheme {
        QuickActionsRow(
            contact = Contact(
                phoneNumber = "(11) 98765-4321"
            )
        )
    }
}