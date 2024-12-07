package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private val iTunesService = RetrofitClient.iTunesService
    private val trackList = ArrayList<Song>()
    lateinit var trackHistoryAdapter: TrackAdapter
    lateinit var trackAdapter: TrackAdapter
    lateinit var searchSong: RecyclerView
    lateinit var searchHistoryRV: RecyclerView
    lateinit var errorIcon: ImageView
    lateinit var errorText: TextView
    lateinit var errorReloadButton: Button
    private var inputEditTextValue: String = EDIT_TEXT_DEF





    fun searchSong(text: String) {
        val nothingFound = getString(R.string.NothingFound)
        val serverErrorSearch = getString((R.string.ConnectProblemNothingFound))



        iTunesService.search(text)
            .enqueue(object : Callback<SongResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<SongResponse>,
                    response: Response<SongResponse>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.results.isNullOrEmpty()) { //ничего не найдено
                            searchSong.visibility = View.GONE
                            Glide.with(application).load(R.drawable.not_found).centerInside()
                                .into(errorIcon)
                            errorIcon.visibility = View.VISIBLE
                            errorText.setText(nothingFound)
                            errorText.visibility = View.VISIBLE
                            errorReloadButton.visibility = View.GONE
                        } else { //что то найдено. надо показать
                            searchSong.visibility = View.VISIBLE
                            trackList.clear()
                            trackList.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                            errorIcon.visibility = View.GONE
                            errorText.visibility = View.GONE
                            errorReloadButton.visibility = View.GONE
                        }
                    } else {
                        searchSong.visibility = View.GONE
                        Glide.with(application).load(R.drawable.server_error).centerInside()
                            .into(errorIcon)
                        errorIcon.visibility = View.VISIBLE
                        errorText.setText(serverErrorSearch)
                        errorText.visibility = View.VISIBLE
                        errorReloadButton.visibility = View.VISIBLE

                    }
                }

                override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                    searchSong.visibility = View.GONE
                    Glide.with(application).load(R.drawable.server_error).centerInside()
                        .into(errorIcon)
                    errorIcon.visibility = View.VISIBLE
                    errorText.setText(serverErrorSearch)
                    errorText.visibility = View.VISIBLE
                    errorReloadButton.visibility = View.VISIBLE
                }

            })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_VALUE, inputEditTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditTextValue = savedInstanceState.getString(EDIT_TEXT_VALUE, EDIT_TEXT_DEF)
    }


    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        errorIcon = findViewById<ImageView>(R.id.searchErrorIcon)
        errorText = findViewById<TextView>(R.id.searchErrorText)
        errorReloadButton = findViewById<Button>(R.id.searchErrorButton)

        val historyView = findViewById<LinearLayout>(R.id.searchHistoryView)
        val historyClearButton = findViewById<Button>(R.id.clearHistoryButton)

        val sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPrefs)

        searchHistoryRV = findViewById<RecyclerView>(R.id.searchHistory_recyclerView)
        searchHistoryRV.layoutManager = LinearLayoutManager(this)

        searchSong = findViewById(R.id.searсh_recyclerView)
        searchSong.layoutManager = LinearLayoutManager(this)

        trackAdapter = TrackAdapter(trackList){
                song -> searchHistory.addSong(song)
            val displayIntent = Intent(this, AudioplayerActivity::class.java)
                displayIntent.putExtra(KEY_CHOSEN_TRACK, Gson().toJson(song))
                startActivity(displayIntent)
        }
        searchSong.adapter = trackAdapter


        trackHistoryAdapter = TrackAdapter(trackList){
                song -> searchHistory.addSong(song)
            val displayIntent = Intent(this, AudioplayerActivity::class.java)
            displayIntent.putExtra(KEY_CHOSEN_TRACK, Gson().toJson(song))
            startActivity(displayIntent)
        }
        searchHistoryRV.adapter = trackHistoryAdapter

        val inputEditText = findViewById<EditText>(R.id.input_edit_text)
        val historySongs = searchHistory.getHistorySong()
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            historyView.visibility  = if (hasFocus && inputEditText.text.isEmpty() && historySongs.isNotEmpty()) View.VISIBLE else View.GONE
            if (hasFocus && inputEditText.text.isEmpty() && historySongs.isNotEmpty()) {
                searchHistoryRV.visibility = View.VISIBLE
                trackAdapter.updateData(historySongs)
            }else {
                searchHistoryRV.visibility = View.GONE
            }

        }


        fun loadHistory(){
            val historySongs = searchHistory.getHistorySong()
            val inputEditText = findViewById<EditText>(R.id.input_edit_text)
            Log.d("History", "Загруженные песни: $historySongs")

            if(historySongs.isEmpty() && inputEditText.hasFocus()){
                searchHistoryRV.visibility = View.GONE
                searchSong.visibility = View.VISIBLE
                historyView.visibility = View.GONE

            } else if (inputEditText.hasFocus()){
                searchHistoryRV.visibility = View.VISIBLE
                searchSong.visibility = View.GONE
                historyView.visibility = View.VISIBLE
                trackHistoryAdapter.updateData(historySongs)
                Log.d("History", "Загрузкааа успешна - $historySongs")
            }

        }
        loadHistory()


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }


        val clearTextButton = findViewById<ImageView>(R.id.clear_text_button)


        if (savedInstanceState != null) {
            inputEditTextValue = savedInstanceState.getString(EDIT_TEXT_VALUE, EDIT_TEXT_DEF)

        }
        inputEditText.setText(inputEditTextValue)


            historyClearButton.setOnClickListener {
                historyView.visibility = View.GONE
                searchHistory.clearHistory()
                loadHistory()
                trackHistoryAdapter.updateData(emptyList())
            }



        clearTextButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard(it)
            errorText.visibility = View.GONE
            errorIcon.visibility = View.GONE
            searchSong.visibility = View.GONE

        }

        errorReloadButton.setOnClickListener {
            searchSong(inputEditTextValue)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSong(inputEditTextValue)

            }
            false
        }

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                //empty
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                clearTextButton.visibility = clearButtonVisibility(charSequence)
                inputEditTextValue = charSequence.toString()

                if (inputEditTextValue.isEmpty()) {
                    loadHistory()
                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                    trackHistoryAdapter.notifyDataSetChanged()
                    errorText.visibility = View.GONE
                    errorIcon.visibility = View.GONE
                    searchSong.visibility = View.GONE
                    searchHistoryRV.visibility = View.VISIBLE



                    //показать историю поиска
                    val historySongs = searchHistory.getHistorySong()
                    if (historySongs.isNotEmpty() && inputEditText.hasFocus()){
                        historyView.visibility = View.VISIBLE
                        trackAdapter.updateData(historySongs)
                    } else {
                        historyView.visibility = View.GONE
                    }
                } else {
                    historyView.visibility = View.GONE
                }


            }

            override fun afterTextChanged(editable: Editable?) {
                //empty
            }
        })
    }


    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    private fun hideKeyboard(view: View) {
        val softKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        softKeyboard.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        const val EDIT_TEXT_VALUE = "EDIT_TEXT_VALUE"
        const val EDIT_TEXT_DEF = ""
    }

}