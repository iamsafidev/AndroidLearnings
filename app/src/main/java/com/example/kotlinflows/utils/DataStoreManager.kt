package com.example.kotlinflows.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.kotlinflows.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val USER_DATASTORE = "datastore"

class DataStoreManager(val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

    companion object {
        val NAME = stringPreferencesKey("NAME")
        val PASSWORD = stringPreferencesKey("PASSWORD")
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit {
            it[NAME] = user.userName
            it[PASSWORD] = user.password
        }
    }

    fun getUser(): Flow<User> {
       return context.dataStore.data.map {
            User(
                userName = it[NAME] ?: "",
                password = it[PASSWORD] ?: ""
            )
        }
    }
}