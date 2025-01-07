package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private var trackListResult: ArrayList<Song>,
    private val onSongClick: (Song) -> Unit) :
    RecyclerView.Adapter<TrackViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_tracklist, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackListResult.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackListResult[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()){
                onSongClick(trackListResult[position])}
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTracks: List<Song>) {
        trackListResult.clear() // Очищаем текущий список
        trackListResult.addAll(newTracks) // Добавляем новые треки
        notifyDataSetChanged() // Уведомляем адаптер об изменениях
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if(isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}