package com.example.guitartabcreator.ui.repositories

import com.example.guitartabcreator.ui.models.RowOfStrings
import com.example.guitartabcreator.ui.models.Tab
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object TabRepository {

    private val tabCache = mutableListOf<Tab>()
    private var cacheInitialized = false

    // returns the list of fireStore avatars whether cached or not
    // adds all the fireStore avatars with that userId to the avatarCache
    suspend fun getTabs(): List<Tab> {
        return if (cacheInitialized){
            tabCache
        } else {
            val snapshot = Firebase.firestore
                .collection("tabs")
                .whereEqualTo("userId", UserRepository.getCurrentUserId())
                .get()
                .await()
            tabCache.addAll(snapshot.toObjects(Tab::class.java))
            cacheInitialized = true
            tabCache
        }
    }

    // takes in the needed values, creates an Tab object with them,
    // adds that object to the fireStore document and tabCache and
    // returns the tab
    suspend fun createTab(
        nameTab: String,
        fretItems: List<RowOfStrings>

    ): Tab {
        val doc = Firebase.firestore.collection("tabs").document()
        val tab = Tab(
            tabName = nameTab,
            fretItems = fretItems,
            id = doc.id,
            userId = UserRepository.getCurrentUserId()
        )
        doc.set(tab).await()
        tabCache.add(tab)
        return tab
    }

    suspend fun deleteTab(tabId: String) {
        // Delete from fireStore
        Firebase.firestore.collection("tabs")
            .document(tabId)
            .delete()
            .await()

        // Remove from cache
        tabCache.removeAll { it.id == tabId }
    }


    // finds the avatar in firebase with that id, updates it with the new info
    // in the Avatar object and same with the avatarCache
    suspend fun updateTab(tab: Tab) {
        Firebase.firestore.collection("tabs")
            .document(tab.id!!)
            .set(tab)
            .await()

        val oldAvatarIndex = tabCache.indexOfFirst {
            it.id == tab.id
        }
        tabCache[oldAvatarIndex] = tab
    }
}