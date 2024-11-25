package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory (val sharedPrefs : SharedPreferences){

    val gson = Gson()
    private val KEY_SEARCH_HISTORY_LIST = "search_history_list"
    private val MAX_HISTORY_SIZE = 10

    private fun getSharedPrefs(key:String):String? {
        return sharedPrefs.getString(key, null)
    }

    fun arrayListToJson(arrayList: List<Song>) : String{
        return gson.toJson(arrayList)
    }

    fun jsonToArrayList(json: String?): ArrayList<Song>{
        return if (json != null) {
            val type = object : TypeToken<ArrayList<Song>>(){}.type
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
    }



    fun addSong(song: Song){
        val songList = getHistorySong().toMutableList()
        songList.removeIf { it.trackId == song.trackId }
        songList.add(0, song)

        if (songList.size > MAX_HISTORY_SIZE) {
            songList.removeAt(songList.size - 1)
        }

        saveHistorySongs(songList)
    }

    fun getHistorySong(): List<Song> {
        val jsonHistory = getSharedPrefs(KEY_SEARCH_HISTORY_LIST) ?: return emptyList()
        return jsonToArrayList(jsonHistory)
    }

    private fun saveHistorySongs (songs: List<Song>) {
        val json = arrayListToJson(songs)
        sharedPrefs.edit().putString(KEY_SEARCH_HISTORY_LIST, json).apply()
    }

    fun clearHistory(){
        sharedPrefs.edit().remove(KEY_SEARCH_HISTORY_LIST).apply()
    }

}

