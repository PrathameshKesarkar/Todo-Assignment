package co.pratham.assignment.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes

fun ViewGroup.inflate(layout: Int): View {
   return LayoutInflater.from(this.context).inflate(layout, this, false)
}