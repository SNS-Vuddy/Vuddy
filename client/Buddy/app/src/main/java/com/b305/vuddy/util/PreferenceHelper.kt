package com.b305.buddy.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceHelper {
    
    fun defaultPrefs(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }
    
    operator fun SharedPreferences.set(key: String, value: String) {
        edit { it.putString(key, value) }
    }
    
    @Suppress("UNCHECKED_CAST")
    operator fun <T> SharedPreferences.get(key: String, defaultValue: T? = null): T {
        return getString(key, defaultValue as? String) as T
    }
    
    fun SharedPreferences.remove(key: String) {
        edit { it.remove(key) }
    }
}