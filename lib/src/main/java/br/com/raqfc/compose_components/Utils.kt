package br.com.raqfc.compose_components

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

class UtilsView private constructor() {
    companion object {
        fun Context.getActivity(): AppCompatActivity? = when (this) {
            is AppCompatActivity -> this
            is ComponentActivity -> baseContext.getActivity()
            is ContextWrapper -> baseContext.getActivity()
            else -> null
        }
    }
}