package com.example.guitartabcreator.ui.components

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.guitartabcreator.ui.viewmodels.EditTabViewModel
import com.example.guitartabcreator.ui.viewmodels.TabItem

internal val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }

// The items we're dragging, dataToDrop's correct values this far is only the character
@Composable
fun DragTarget(
    modifier: Modifier = Modifier,
    dataToDrop: TabItem,
    viewModel: EditTabViewModel,
    content: @Composable (() -> Unit)
) {
    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    val currentState = LocalDragTargetInfo.current
    var firstPosition = 0.dp

    Log.d("Start: currentState.dragPosition", "${currentState.dragPosition}")
    Log.d("Start: currentState.dragOffset", "${currentState.dragOffset}")

    Box(modifier = modifier
        .onGloballyPositioned {
            currentPosition = it.localToWindow(
                Offset.Zero
            )
            firstPosition = Dp(it.positionInRoot().x)
        }
        .pointerInput(Unit) {
            detectDragGestures(onDragStart = {
                viewModel.startDragging()
                currentState.isDragging = true
                currentState.dragPosition = currentPosition + it
                currentState.draggableComposable = content
            }, onDrag = { change, dragAmount ->
                change.consume()
                currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
            }, onDragEnd = {
                // Don't know the rowId or string values yet so initializing them to 0
                // firstPosition + currentState.dragOffset.x should be the offset relative to far left
                currentState.dataToDrop = TabItem(firstPosition + Dp(currentState.dragOffset.x), 0, dataToDrop.tabStringVal, 0, 0)
                viewModel.stopDragging()
                currentState.isDragging = false
                currentState.dragOffset = Offset.Zero
                currentState.dragPosition = Offset.Zero
            }, onDragCancel = {
                viewModel.stopDragging()
                currentState.dragOffset = Offset.Zero
                currentState.isDragging = false
                currentState.dragPosition = Offset.Zero
            })
        }) {
        content()
    }
    Log.d("End: currentState.dragPosition", "${currentState.dragPosition}")
    Log.d("End: currentState.dragOffset", "${currentState.dragOffset}")
}


//Receiving the Item I think
@Composable
fun <T> DropItem(
    modifier: Modifier,
    content: @Composable (BoxScope.(isInBound: Boolean, data: T?) -> Unit)
) {

    val dragInfo = LocalDragTargetInfo.current
    val dragPosition = dragInfo.dragPosition
    val dragOffset = dragInfo.dragOffset
    var isCurrentDropTarget by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier.onGloballyPositioned {
        it.boundsInWindow().let { rect ->
            isCurrentDropTarget = rect.contains(dragPosition + dragOffset)
        }
    }) {
        val data =
            if (isCurrentDropTarget && !dragInfo.isDragging) dragInfo.dataToDrop as T? else null
        content(isCurrentDropTarget, data)
    }
}

internal class DragTargetInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dataToDrop by mutableStateOf<Any?>(null)
}