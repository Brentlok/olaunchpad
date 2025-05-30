package expo.modules.launchpad
import android.content.Intent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.net.toUri
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

data class Contact(
    val label: String,
    val phoneNumber: String?,
    val icon: ImageBitmap?
)

data class ContactsState(
    val contacts: List<Contact>,
    val onContactPress: (contact: Contact) -> Unit,
    val callPhone: (phoneNumber: String) -> Unit
)

@Composable
fun getContactsState(launchpad: LaunchpadState): ContactsState {
    var allContacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    val contacts = remember(launchpad.searchText.text, allContacts) {
        filterAndTake(
            list = allContacts,
            callback =  { it.label.contains(launchpad.searchText.text, ignoreCase = true) || it.phoneNumber.toString().contains(launchpad.searchText.text, ignoreCase = true) },
            count = 3
        )
    }

    fun callPhone(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:${phoneNumber}".toUri()
        }
        launchpad.context.startActivity(intent)
    }

    fun onContactPress(contact: Contact) {
        if (contact.phoneNumber != null) {
            launchpad.saveLastAction(HistoryItem(
                label = "Call ${contact.label}",
                type = "phone",
                actionValue = contact.phoneNumber
            ))
            callPhone(contact.phoneNumber)
        }

        launchpad.closeLaunchpad()
    }

    LaunchedEffect(key1 = true) {
        if (launchpad.settings.isContactsEnabled) {
            coroutineScope.launch {
                try {
                    val fetchedContactsList = getContacts(launchpad.context)
                    allContacts = fetchedContactsList
                } catch (e: Exception) {
                    allContacts = emptyList()
                }
            }
        }
    }

    return ContactsState(
        contacts = contacts,
        onContactPress = ::onContactPress,
        callPhone = ::callPhone
    )
}

@Composable
fun ContactView(contact: Contact, contactsState: ContactsState) {
    LaunchpadRowItem(
        icon = contact.icon,
        label = contact.label,
        onClick = { contactsState.onContactPress(contact) },
        subLabel = contact.phoneNumber
    )
}