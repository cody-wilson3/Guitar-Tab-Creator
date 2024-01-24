package com.example.guitartabcreator.ui.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.guitartabcreator.ui.models.RowOfStrings
import com.example.guitartabcreator.ui.viewmodels.EditTabViewModel
import com.example.guitartabcreator.ui.viewmodels.TabItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.text.font.FontWeight


@Composable
fun FormField(
    formRow: RowOfStrings,
//    onValueChange: (List<RowOfStrings>) -> Unit,
    screenWidth: Int,
    viewModel: EditTabViewModel,
) {
    StringsRow(screenWidth, viewModel, formRow)
}

@Composable
fun StringsRow(screenWidth: Int, viewModel: EditTabViewModel, formRow: RowOfStrings) {

    // background for a single whole row of strings
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        verticalArrangement = Arrangement.Top
    ) {
            // for each string in a row
            for (i in 1..5) {
                var firstOrLast = false
                if (i == 5) {
                    firstOrLast = true
                }
                Row(

                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    // for each measure column
                    for (j in 1..3) {
                        OneThirdLine(
                            firstOrLast,
                            j,
                            screenWidth,
                            viewModel,
                            formRow.rowNumber,
                            i,
                            formRow
                        )
                    }
                }

            }

    }
}

@Composable
fun OneThirdLine(
    firstOrLast: Boolean,
    thirdNum: Int,
    screenWidth: Int,
    viewModel: EditTabViewModel,
    rowNumber: Int,
    stringNumber: Int,
    formRow: RowOfStrings
    ) {

    Box(
        modifier = Modifier
            .width(Dp(screenWidth / 3f))
            .height(20.dp)
    ) {

        DropItem<TabItem>(
            modifier = Modifier
                .width(Dp(screenWidth / 3f))
                .height(20.dp)
            // THIS IS WHERE we're receiving the tabItem
        ) { isInBound, tabItem ->
            if (tabItem != null) {
                LaunchedEffect(key1 = tabItem) {
                    // This is where the TabItem is finalized ****************************************
                    val newTabItem = TabItem(tabItem.offsetX, rowNumber, tabItem.tabStringVal, stringNumber, thirdNum)
                    viewModel.addTabItem(newTabItem)
                }
            }
            val itemsList = formRow.stringList[stringNumber - 1].oneThirdsList[thirdNum - 1].tabStringItems

            Text(
                text = "${itemsList.count()}"
            )

                for (item in itemsList) {
                    val newOffset = item.offSetX
                    Log.d("item.offset: ", "${item.offSetX}")
                    Text(
                        modifier = Modifier.offset(x = newOffset),
                        text = item.whichItem,
                        fontWeight = FontWeight.Bold,
                    )
                }

                // If the name Box is in the right position to be dropped
                if (isInBound) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(0.5f)),
                    ) { }
                }

                // Left vertical line of a box
                if (!firstOrLast) {
                    Canvas(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),) {
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, 35f),
                            end = Offset(0f, 90f),
                            strokeWidth = 5f
                        )
                    }
                }

                // Line where Items should go --here
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),) {
                    drawLine(
                    color = Color.Black,
                    start = Offset(0f, 35f),
                    end = Offset(size.width,35f),
                    strokeWidth = 5f
                    )
                }

                //Right vertical line
                if (!firstOrLast) {
                    Canvas(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),) {
                        drawLine(
                            color = Color.Black,
                            start = Offset(size.width, 35f),
                            end = Offset(size.width, 90f),
                            strokeWidth = 5f
                        )
                    }
                }


        }
    }
}

//@Preview
//@Composable
//fun FormFieldPreview() {
//    // Example usage of FormField
//    FormField(
//        formRow = n,
//        onValueChange = {},
//        error = false
//    )
//}
