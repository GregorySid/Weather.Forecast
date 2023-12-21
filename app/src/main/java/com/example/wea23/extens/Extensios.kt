package com.example.wea23.extens

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.isPermGranted(p: String): Boolean {
    return ContextCompat.checkSelfPermission(
         activity as AppCompatActivity, p) == PackageManager.PERMISSION_GRANTED
}