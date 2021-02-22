package co.pratham.assignment

import androidx.annotation.Keep

@Keep
data class Todo(
    @JvmField
    val title: String?= null,
    @JvmField
    val description: String? = null)