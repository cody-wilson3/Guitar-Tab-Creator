package com.example.guitartabcreator.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    inputValue: String,
    onInputChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text("Enter a number")
        },
        text = {
            TextField(
                value = inputValue,
                onValueChange = {
                    onInputChange(it)
                },
                label = {
                    Text("Number")
                },
                keyboardOptions = KeyboardOptions(

                    capitalization = KeyboardCapitalization.None
                ),
                visualTransformation = VisualTransformation.None
            )
        },
        confirmButton = {
            IconButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Icon(Icons.Default.Done, contentDescription = "Save")
            }
        },
        dismissButton = {
            IconButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cancel")
            }
        }
    )
}