package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val ITunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)

    private val trackList = ArrayList<Song>()

    lateinit var trackAdapter: TrackAdapter

    lateinit var searchSong: RecyclerView
    lateinit var errorIcon: ImageView
    lateinit var errorText: TextView
    lateinit var errorReloadButton: Button

    private var inputEditTextValue: String = EDIT_TEXT_DEF

    fun searchSong(text: String) {
        val nothingFound = "Ничего не нашлось"
        val serverErrorSearch =
            "Проблемы со связью\\n\\nЗагрузка не удалась. Проверьте подключение к интернету"

        iTunesService.search(text)
            .enqueue(object : Callback<SongResponse> {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        errorIcon = findViewById<ImageView>(R.id.searchErrorIcon)
        errorText = findViewById<TextView>(R.id.searchErrorText)
        errorReloadButton = findViewById<Button>(R.id.searchErrorButton)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val inputEditText = findViewById<EditText>(R.id.input_edit_text)
        val clearTextButton = findViewById<ImageView>(R.id.clear_text_button)

        if (savedInstanceState != null) {
            inputEditTextValue = savedInstanceState.getString(EDIT_TEXT_VALUE, EDIT_TEXT_DEF)

        }
        inputEditText.setText(inputEditTextValue)


        clearTextButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard(it)
            errorText.visibility = View.GONE
            errorIcon.visibility = View.GONE
            searchSong.visibility = View.VISIBLE
        }

        errorReloadButton.setOnClickListener {
            searchSong(inputEditTextValue)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSong(inputEditTextValue)
                true
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
                // empty
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
                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                    errorText.visibility = View.GONE
                    errorIcon.visibility = View.GONE
                    searchSong.visibility = View.VISIBLE

                }
            }

            override fun afterTextChanged(editable: Editable?) {
                //empty
            }
        })

        searchSong = findViewById<RecyclerView>(R.id.seacrh_recyclerView)
        searchSong.layoutManager = LinearLayoutManager(this@SearchActivity)

        trackAdapter = TrackAdapter(trackList)
        searchSong.adapter = trackAdapter

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