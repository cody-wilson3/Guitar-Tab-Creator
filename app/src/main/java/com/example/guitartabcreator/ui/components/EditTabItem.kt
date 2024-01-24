package com.example.guitartabcreator.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun EditTabItem(icon: String) {
    var offsetX = remember { Animatable(0f) }
    var offsetY = remember { Animatable(0f)}

    fun clamp(value: Float, min: Float = -45f, max: Float = 45f): Float {
        if (value >= max) return max
        if (value <= min) return min
        return value
    }

    Column(
        modifier = Modifier
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        val pointerId = awaitPointerEventScope {
                            awaitFirstDown().id
                        }
                        awaitPointerEventScope {
                            horizontalDrag(pointerId) {
                                launch {
                                        offsetX.snapTo(offsetX.value + it.positionChange().x)
                                        offsetY.snapTo(offsetY.value + it.positionChange().x)
                                }
                            }
                        }
                        // logic for animating when the finger is lifted up
//                        launch {
//                            rotation.animateTo(0f)
//                            if (rotation.value == 0f && offsetX.value < -200 && currCard < cardList.size - 1)
//                            {
//                                offsetX.animateTo(-1000f)
//
//                            } else if (rotation.value == 0f && offsetX.value > 200 && currCard > 0)
//                            {
//                                offsetX.animateTo(1000f)
//                            } else {
//                                offsetX.animateTo(0f)
//                            }
//                        }

                    }

                }

            }
    ) {
        Text(
            text = icon,
            style = TextStyle(
                fontSize = 20.sp,
            )
        )
    }

}