package com.jkl.familytrack.utils.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(layoutResourceId:Int): View {
    return LayoutInflater.from(context).inflate(layoutResourceId,this,false)
}