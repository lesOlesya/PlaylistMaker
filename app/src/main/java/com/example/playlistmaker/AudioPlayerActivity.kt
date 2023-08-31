package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AudioPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val trackId = intent.getIntExtra("TrackId", 0)
        val track = getTrack(trackId)


        val backButton = findViewById<ImageButton>(R.id.audio_player_back)
        val ivArtwork =  findViewById<ImageView>(R.id.artwork_iv_audio_player)
        val tvTrackName = findViewById<TextView>(R.id.track_name_tv_audio_player)
        val tvArtistName = findViewById<TextView>(R.id.artist_name_tv_audio_player)
        val tvTrackTime = findViewById<TextView>(R.id.track_time_tv_audio_player)
        val tvDuration = findViewById<TextView>(R.id.duration_tv_audio_player)
        val tvAlbum = findViewById<TextView>(R.id.album_tv_audio_player)
        val tvYear = findViewById<TextView>(R.id.year_tv_audio_player)
        val tvGenre = findViewById<TextView>(R.id.genre_tv_audio_player)
        val tvCountry = findViewById<TextView>(R.id.country_tv_audio_player)

        backButton.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(track!!.getCoverArtwork())
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8F, ivArtwork.context)))
            .into(ivArtwork)

        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        //Track Time
        tvDuration.text = track.getTrackTime()
        tvAlbum.text = track.collectionName ?: " "
        tvYear.text = track.releaseDate?.substring(0, 4) ?: " "
        tvGenre.text = track.primaryGenreName ?: " "
        tvCountry.text = track.country ?: " "
    }

    private fun getTrack(trackId: Int) : Track? {
        val sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        val tracks: ArrayList<Track> = Gson().fromJson(json, type)

        for (i in 0 until tracks.size) {
            if (trackId == tracks[i].trackId) return tracks[i]
        }
        return null
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}

