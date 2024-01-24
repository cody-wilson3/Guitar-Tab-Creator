package com.example.guitartabcreator.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import com.example.guitartabcreator.ui.repositories.TabRepository
import com.example.guitartabcreator.ui.models.RowOfStrings
import com.example.guitartabcreator.ui.models.TabStringItem

data class TabItem(
    val offsetX: Dp,
    val rowId: Int,
    val tabStringVal:String,
    val stringNumber: Int,
    val whichThird: Int
)

class EditTabScreenState {
    var id by mutableStateOf("")
//    var userId by mutableStateOf("")
    var tabName by mutableStateOf("")
    var rowsList = mutableStateListOf<RowOfStrings>()

    var saveSuccess by mutableStateOf(false)
    var tabNameError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var tabError by mutableStateOf(false)
}

class EditTabViewModel(application: Application) : AndroidViewModel(application) {
    val uiState = EditTabScreenState()
    private var id: String? = null

    // From DragAndDrop ----------------------------------------------------------------------
    private var isCurrentlyDragging by mutableStateOf(false)

    var items = mutableListOf(TabItem(0.dp, 0, "/", 0, 0),
        TabItem(0.dp, 0, "\\", 0, 0),
        TabItem(0.dp, 0, "X", 0, 0),
        TabItem(0.dp, 0, "1", 0, 0),)

        private set


    fun startDragging(){
        isCurrentlyDragging = true
    }
    fun stopDragging(){
        isCurrentlyDragging = false
    }

    fun addTabItem(tabItem: TabItem){
        val newItem = TabStringItem(
            tabItem.tabStringVal,
            tabItem.stringNumber,
            tabItem.offsetX,
            tabItem.whichThird
        )
        uiState.rowsList[tabItem.rowId].addTabStringItem(newItem)
    }
// End from DragAndDrop -------------------------------------------------------------------

    // if a tab with that id is in repository, updates the uiState,
    // otherwise, just returns
    suspend fun setUpInitialState(id: String?) {
        if (id == null || id == "new") {
            val defaultRow0 = RowOfStrings(rowNumber = 0)
            val defaultRow1 = RowOfStrings(rowNumber = 1)
            val defaultRow2 = RowOfStrings(rowNumber = 2)
            val defaultRow3 = RowOfStrings(rowNumber = 3)
            uiState.rowsList.add(defaultRow0)
            uiState.rowsList.add(defaultRow1)
            uiState.rowsList.add(defaultRow2)
            uiState.rowsList.add(defaultRow3)
            return
        }
        this.id = id
        val tab = TabRepository.getTabs().find { it.id == id} ?: return
        uiState.rowsList.clear()
        tab.fretItems?.let { uiState.rowsList.addAll(it) }
        uiState.tabName = tab.tabName ?: ""
    }

    // saves the avatar whether we're creating new or editing existing based on
    // the id. Calls "CreateAvatar" or "UpdateAvatar" which both update fireStore
    suspend fun saveTab() {
        // if there are any errors, don't do anything
        if (uiState.tabError) return
        uiState.errorMessage = ""
        uiState.tabError = false

        if (uiState.tabName.isEmpty()){
            uiState.tabNameError = true
            uiState.errorMessage = "Title cannot be Blank"
            return
        }

        // create new tab if id isn't found
        if (id == null) {
            TabRepository.createTab(
                uiState.tabName,
                uiState.rowsList,
            )

        } else {
            // update existing avatar with new information
            val tabToUpdate = TabRepository.getTabs().find { it.id == id } ?: return
            TabRepository.updateTab(
                tabToUpdate.copy(
                    id = tabToUpdate.id,
                    userId = tabToUpdate.userId,
                    tabName = uiState.tabName,
                    fretItems = uiState.rowsList
                )
            )
        }
        uiState.saveSuccess = true
    }
}
