package com.example.guitartabcreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guitartabcreator.ui.viewmodels.DeleteConfirmationViewModel

@Composable
fun DeleteConfirmationScreenLogic(
    navHostController: NavHostController,
    id: String,
    onDeleteConfirmed: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Confirmation",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Are you sure you want to delete this item?")
                Text("This action cannot be undone.")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDeleteConfirmed()
                    navHostController.navigate("home?id=${id}")
                }
            ) {
                Text("Delete", color = Color.Red)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun DeleteConfirmationScreen(navHostController: NavHostController, theId: String?) {
    var isDialogVisible by remember { mutableStateOf(true) }
    val viewModel: DeleteConfirmationViewModel = viewModel()


    // Your existing Compose code...

    // Delete confirmation screen
    if (isDialogVisible) {
        DeleteConfirmationScreenLogic(
            navHostController,
            theId!!,
            onDeleteConfirmed = {
                    viewModel.deleteTab(theId)
            },
            onDismiss = {
                isDialogVisible = false
                navHostController.popBackStack()
            }
        )
    }
}
