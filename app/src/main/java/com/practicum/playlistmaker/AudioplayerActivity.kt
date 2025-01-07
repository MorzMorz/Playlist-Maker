package com.practicum.playlistmaker

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore.Audio.Media
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageButton
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

private var mediaPlayer = MediaPlayer()
private lateinit var playButton: ImageView
private lateinit var handler: Handler
private lateinit var currentTiming: TextView
private lateinit var  setTimingRunnable: Runnable

class AudioplayerActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        playButton = findViewById(R.id.playSongButton)


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


        preparePlayer(track)

        playButton.setOnClickListener{
            when (playerState) {
                STATE_PLAYING -> pausePlayer()
                STATE_PREPARED,
                STATE_PAUSED -> startPlayer()
            }
        }

        currentTiming = findViewById<TextView>(R.id.currentSongTime)
        handler = Handler(Looper.getMainLooper())
        setTimingRunnable = Runnable { setCurrentTiming() }

    }


    private fun preparePlayer(track: Song){
        mediaPlayer = MediaPlayer()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {

            playerState = STATE_PREPARED
            handler.removeCallbacks(setTimingRunnable)
            playButton.setImageResource(R.drawable.playbutton)
            currentTiming.text = getString(R.string.ZeroTiming)
        }
    }

    private fun startPlayer(){
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playButton.setImageResource(R.drawable.pausebutton)
        handler.postDelayed(setTimingRunnable, 400L)
    }

    private fun pausePlayer(){
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playButton.setImageResource((R.drawable.playbutton))
        handler.removeCallbacks(setTimingRunnable)
    }

    private fun setCurrentTiming(){
        val time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        currentTiming.text = time
        handler.postDelayed(setTimingRunnable,300L)
    }

     override fun onPause(){
         super.onPause()
         pausePlayer()
     }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(setTimingRunnable)
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var playerState = STATE_DEFAULT
}