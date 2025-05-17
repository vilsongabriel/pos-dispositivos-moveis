package dev.onofre.appcontatos.ui.contact.details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.onofre.appcontatos.ui.contact.composables.DefaultErrorContent
import dev.onofre.appcontatos.ui.contact.composables.DefaultLoadingContent
import dev.onofre.appcontatos.ui.contact.composables.AppBar
import dev.onofre.appcontatos.ui.contact.details.composables.ContactDetails

@Composable
fun ContactDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactDetailsViewModel = viewModel()
) {
    val contentModifier = Modifier.fillMaxSize()
    if (viewModel.state.isLoading) {
        DefaultLoadingContent(
            modifier = contentModifier,
        )
    } else if (viewModel.state.hasErrorLoading) {
        DefaultErrorContent(
            modifier = contentModifier,
            onTryAgainPressed = viewModel::loadContact
        )
    } else {
        Scaffold (
            modifier = contentModifier,
            topBar = {
                AppBar(
                    contact = viewModel.state.contact,
                    onBackPressed = {},
                    onEditPressed = {},
                    onFavoritePressed = {},
                    onDeletePressed = {},
                )
            }
        ) {
            innerPadding ->
            ContactDetails(
                modifier = Modifier.padding(innerPadding),
                contact = viewModel.state.contact,
                onContactInfoPressed = {}
            )
        }
    }
}