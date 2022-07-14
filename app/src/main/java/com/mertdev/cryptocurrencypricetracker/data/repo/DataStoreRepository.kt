package com.mertdev.cryptocurrencypricetracker.data.repo

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mertdev.cryptocurrencypricetracker.utils.Constants.DATA_STORE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context){

    private object PreferenceKeys{
        val interval = stringPreferencesKey("interval")
    }

    suspend fun saveToDataStore(interval: String){
        context.dataStore.edit { preference ->
            preference[PreferenceKeys.interval] = interval
        }
    }

    val readFromDataStore: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException){
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map { preference ->
            val interval = preference[PreferenceKeys.interval] ?: "30"
            interval
        }

}
