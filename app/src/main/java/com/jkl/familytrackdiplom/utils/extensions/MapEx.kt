package com.jkl.familytrack.utils.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.jkl.familytrackdiplom.R
import de.hdodenhof.circleimageview.CircleImageView

    fun createMarker(context:Context,map: GoogleMap, lat:Double, lng:Double, iconResource:Int): Marker? {
       return map.addMarker(
           MarkerOptions()
               .position(LatLng(lat,lng))
               .anchor(0.5f, 0.5f)
               .title("")
               .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(context,iconResource)))
       )
    }

    fun getMarkerBitmapFromView(context:Context,@DrawableRes resId: Int): Bitmap {

        val customMarkerView = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
           .inflate(
               R.layout.layout_custom_marker,
               null
           )

       val markerImageView = customMarkerView.findViewById(R.id.imgFamilyMember) as CircleImageView
       markerImageView.setImageResource(resId)
       customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
       customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)
       customMarkerView.buildDrawingCache()
       val returnedBitmap = Bitmap.createBitmap(
           customMarkerView.measuredWidth, customMarkerView.measuredHeight,
           Bitmap.Config.ARGB_8888
       )
       val canvas = Canvas(returnedBitmap)
       canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
       val drawable = customMarkerView.background
        drawable?.draw(canvas)
       customMarkerView.draw(canvas)
       return returnedBitmap

    }

    fun zoomToAllMarkers(googleMap: GoogleMap,markers:ArrayList<Marker>)
    {
        val builder = LatLngBounds.Builder()
        for (marker in markers) {
            builder.include(marker.position)
        }
        val bounds = builder.build()
        val padding = 300
        val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(bounds,padding)
        googleMap.animateCamera(cameraUpdateFactory)
    }