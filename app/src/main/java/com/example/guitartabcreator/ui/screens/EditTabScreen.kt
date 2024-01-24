package com.example.guitartabcreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guitartabcreator.ui.components.DragTarget
import com.example.guitartabcreator.ui.components.DragTargetInfo
import com.example.guitartabcreator.ui.components.FormField
import com.example.guitartabcreator.ui.components.InputDialog
import com.example.guitartabcreator.ui.components.LocalDragTargetInfo
import com.example.guitartabcreator.ui.models.RowOfStrings
import com.example.guitartabcreator.ui.navigation.Routes
import com.example.guitartabcreator.ui.viewmodels.EditTabViewModel
import com.example.guitartabcreator.ui.viewmodels.TabItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTabScreen(navHostController: NavHostController, id: String?) {
    val viewModel: EditTabViewModel = viewModel()
    val state = viewModel.uiState
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var isDialogVisible by remember { mutableStateOf(false) }
    var inputValue by remember { mutableStateOf(viewModel.items[viewModel.items.count() - 1].tabStringVal) }
    var lastTabItem by remember { mutableStateOf(viewModel.items[viewModel.items.count() - 1]) }
    val screenWidth = LocalConfiguration.current.screenWidthDp

    LaunchedEffect(true){
        viewModel.setUpInitialState(id)
    }

    val otherState = remember { DragTargetInfo() }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            navHostController.popBackStack()
        }
    }

    // Tab Name field and Something Drop Down --------------------------------------
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = state.tabName,
            onValueChange = { state.tabName = it },
            placeholder = { Text(text = "Tab Name") },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = { state.rowsList.add(RowOfStrings(rowNumber = state.rowsList.count())) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Add Row")
            }
        }
    }
    // end -------------------------------------------------------------------------

    CompositionLocalProvider(
        LocalDragTargetInfo provides otherState
    ) {
        Column(
            modifier = Modifier
                .padding(top = 65.dp)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                )
                {
                    viewModel.items.forEach { charTabItem ->
                        // each Color Name Box we're dragging
                        if (viewModel.items.indexOf(charTabItem) != viewModel.items.count() - 1) {
                            DragTarget(
                                dataToDrop = charTabItem,
                                viewModel = viewModel
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 35.dp, height = 35.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .shadow(5.dp, RoundedCornerShape(15.dp))
                                        .background(Color.LightGray, RoundedCornerShape(15.dp)),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = charTabItem.tabStringVal,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }

                    DragTarget(
                        dataToDrop = lastTabItem,
                        viewModel = viewModel
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 35.dp, height = 35.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .shadow(5.dp, RoundedCornerShape(15.dp))
                                .background(Color.LightGray, RoundedCornerShape(15.dp))
                                .clickable {
                                    isDialogVisible = true
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = lastTabItem.tabStringVal,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    if (isDialogVisible) {
                        InputDialog(
                            onDismiss = { isDialogVisible = false },
                            onConfirm = {
                                viewModel.items.removeAt(viewModel.items.count() - 1)
                                viewModel.items.add(TabItem(0.dp, 0, inputValue, 0, 0))
                                lastTabItem = viewModel.items[viewModel.items.count() - 1]
                                isDialogVisible = false
                            },
                            inputValue = inputValue,
                            onInputChange = { inputValue = it }
                        )
                    }

                }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
// for each of the string Rows -----------------------------------------------------------
                for (oneRow in state.rowsList) {
                    FormField(
                        formRow = oneRow,
//                        onValueChange = { state.rowsList = it as SnapshotStateList<RowOfStrings> },
                        screenWidth = screenWidth,
                        viewModel = viewModel
                        )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    navHostController.navigate(Routes.appNavigation.route)
                }) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    scope.launch {
                        viewModel.saveTab()
                    }
                }) {
                    Text(text = "Save")
                }
            }
            Text(
                text = state.errorMessage,
                style = TextStyle(color = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )
        }
        if (otherState.isDragging) {
            var targetSize by remember {
                mutableStateOf(IntSize.Zero)
            }
            Box(modifier = Modifier
                .graphicsLayer {
                    val offset = (otherState.dragPosition + otherState.dragOffset)
                    scaleX = 1.3f
                    scaleY = 1.3f
                    alpha = if (targetSize == IntSize.Zero) 0f else .9f
                    translationX = offset.x.minus(targetSize.width / 2)
                    translationY = offset.y.minus((targetSize.height / 2) + 300f)
                }
                .onGloballyPositioned {
                    targetSize = it.size
                }
            ) {
                otherState.draggableComposable?.invoke()
            }
        }
    }
}


