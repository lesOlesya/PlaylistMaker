package com.example.playlistmaker.data.repositories

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.TrackCoverRepository
import com.example.playlistmaker.domain.models.Track

class TrackCoverRepositoryImpl(
    private val context: Context, private val track: Track,
    private val placeForCover: ImageView
) : TrackCoverRepository {

    override fun getTrackCover() {
        Glide.with(context)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8F, placeForCover.context)))
            .into(placeForCover)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}