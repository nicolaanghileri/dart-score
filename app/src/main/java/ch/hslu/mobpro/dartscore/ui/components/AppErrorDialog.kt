package ch.hslu.mobpro.dartscore.ui.components


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AppErrorDialog(
    message: String?,
    onDismiss: () -> Unit,
    title: String = "Invalid input"
) {
    if (message != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            },
            confirmButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("OK")
                }
            }
        )
    }
}