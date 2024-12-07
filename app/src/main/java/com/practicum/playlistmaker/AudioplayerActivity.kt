package com.practicum.playlistmaker

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }


        val trackJson = intent.getStringExtra(KEY_CHOSEN_TRACK)
        val track = Gson().fromJson(trackJson, Song::class.java)



        val trackCoverView = findViewById<ImageView>(R.id.trackAlbumArt)
        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(CenterCrop())
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.baseline_gesture_24)
            .into(trackCoverView)

        val trackNameView = findViewById<TextView>(R.id.songName)
        trackNameView.text = track.trackName

        val artistNameView = findViewById<TextView>(R.id.artistName)
        artistNameView.text = track.artistName

        val durationView = findViewById<TextView>(R.id.durationSongValue)
        durationView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val albumNameView = findViewById<TextView>(R.id.albumSongValue)
        albumNameView.text = track.collectionName

        val yearView = findViewById<TextView>(R.id.songYearValue)
        val releaseDate = track.releaseDate
        yearView.text = if (releaseDate != null && releaseDate.length >= 4) {
            releaseDate.substring(0, 4)
        } else {
            ""
        }



        val genreView = findViewById<TextView>(R.id.songGanreValue)
        genreView.text = track.primaryGenreName

        val countryView = findViewById<TextView>(R.id.songCountryValue)
        countryView.text = track.country

    }
}