package com.bharadwaj.imagevideopicker.utils

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.bharadwaj.imagevideopicker.R

object Globals {

    fun customToast(context: Context, message: String, isGreen: Boolean) {
        if (isGreen) {
            customToastGreen(context, message)
        } else {
            customToastRed(context, message)
        }
    }

    fun savePreference(ctx: Context, key: String, value: String) {
        val pref = ctx.getSharedPreferences(ctx.packageName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun loadPreference(ctx: Context, key: String): String {
        val pref = ctx.getSharedPreferences(ctx.packageName, Context.MODE_PRIVATE)
        return pref.getString(key, "")?:""
    }

    fun customToast(context: Context, message: String, colorResId: Int): Toast {
        val layout: View = LayoutInflater.from(context).inflate(R.layout.toast_layout, null)
        val image = layout.findViewById<View>(R.id.image) as ImageView
        image.setImageResource(R.drawable.ic_launcher_background)
        val text = layout.findViewById<View>(R.id.text) as TextView
        text.text = message
        val mcvParent = layout.findViewById<View>(R.id.mcv_parent) as CardView
        mcvParent.setCardBackgroundColor(context.resources.getColor(colorResId))
        val toast = Toast(context)
        try {

            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 25)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout
            toast.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        log(context, message)
        return toast
    }

    fun log(context: Context, message: String) {
        Log.d(context.packageName, message)
    }

    fun customToastRed(context: Context, message: String) {
        customToast(context, message, R.color._EBA096)
    }

    fun customToastGreen(context: Context, message: String) {
        customToast(context, message, R.color._9EE6A1)
    }
}