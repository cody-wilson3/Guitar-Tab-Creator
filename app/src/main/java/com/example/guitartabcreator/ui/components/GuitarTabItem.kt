package com.example.guitartabcreator.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guitartabcreator.ui.models.Tab

@Composable
fun GuitarTabItem(
    tab: Tab,
    onEditPressed: () -> Unit = {},
    onDeletePressed: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = tab.tabName ?: "",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Row {
                    IconButton(onClick = onDeletePressed) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Button")
                    }
                    IconButton(onClick = onEditPressed) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Button")
                    }
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(8.dp))

            Divider()
        }
    }
}


