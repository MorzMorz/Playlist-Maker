package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class TrackViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
    private val itemAlbumArt: ImageView = itemView.findViewById(R.id.search_tracklist_albumArt)
    private val itemSongName: TextView = itemView.findViewById(R.id.search_tracklist_songName)
    private val itemArtistName: TextView = itemView.findViewById(R.id.search_tracklist_artistName)
    private val itemSongTime: TextView = itemView.findViewById(R.id.search_tracklist_songTime)



}