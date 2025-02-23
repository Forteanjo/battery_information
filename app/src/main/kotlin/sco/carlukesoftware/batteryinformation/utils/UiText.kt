package sco.carlukesoftware.batteryinformation.utils

import android.content.Context
import androidx.annotation.StringRes

sealed interface UiText {

    data class StringResource(@StringRes val id: Int) : UiText

    data class SimpleString(val text: String) : UiText

    data object EmptyString : UiText

    fun getString(context: Context): String =
        when (this) {
            is StringResource -> context.getString(id)
            is SimpleString -> text
            is EmptyString -> ""
        }
}
