package dev.onofre.appcontatos.ui.contact.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.onofre.appcontatos.R
import dev.onofre.appcontatos.data.Contact
import dev.onofre.appcontatos.ui.theme.AppContatosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    isDeleting: Boolean = false,
    contact: Contact,
    onBackPressed: () -> Unit,
    onDeletePressed: () -> Unit,
    onEditPressed: () -> Unit,
    onFavoritePressed: () -> Unit,
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = { Text("") },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            IconButton(
                onClick = onBackPressed
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.appbar_back_label)
                )
            }
        },
        actions = {
            if (isDeleting) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(16.dp),
                    strokeWidth = 2.dp
                )
            } else {
                IconButton(
                    onClick = onEditPressed
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.appbar_edit_label)
                    )
                }
                FavoriteIconButton(
                    isFavorite = contact.isFavorite,
                    onPressed = onFavoritePressed
                )
                IconButton(
                    onClick = onDeletePressed
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.appbar_delete_label)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AppBarPreview() {
    AppContatosTheme {
        AppBar (
            contact = Contact(),
            onBackPressed = {},
            onDeletePressed = {},
            onEditPressed = {},
            onFavoritePressed = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBarDeletingPreview() {
    AppContatosTheme {
        AppBar (
            contact = Contact(),
            isDeleting = true,
            onBackPressed = {},
            onDeletePressed = {},
            onEditPressed = {},
            onFavoritePressed = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBarFavoritePreview() {
    AppContatosTheme {
        AppBar (
            contact = Contact(
                isFavorite = true,
            ),
            onBackPressed = {},
            onDeletePressed = {},
            onEditPressed = {},
            onFavoritePressed = {},
        )
    }
}