package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val itemAlbumArt: ImageView = itemView.findViewById(R.id.search_tracklist_albumArt)
    private val itemSongName: TextView = itemView.findViewById(R.id.search_tracklist_songName)
    private val itemArtistName: TextView = itemView.findViewById(R.id.search_tracklist_artistName)
    private val itemSongTime: TextView = itemView.findViewById(R.id.search_tracklist_songTime)


    fun bind(model: Track) {
        itemSongName.text = model.trackName
        itemArtistName.text = model.artistName
        itemSongTime.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.baseline_photo_24)
            .transform(CenterCrop())
            .transform(RoundedCorners(2))
            .into(itemAlbumArt)
    }
}