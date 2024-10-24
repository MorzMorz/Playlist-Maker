package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private var inputEditTextValue: String = EDIT_TEXT_DEF

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

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                clearTextButton.visibility = clearButtonVisibility(charSequence)
                inputEditTextValue = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable?) {
                //empty
            }
        })


        val recyclerView = findViewById<RecyclerView>(R.id.seacrh_recyclerView)
        val trackAdapter = TrackAdapter(tracklist)
        recyclerView.adapter = trackAdapter

    }

    val tracklist = listOf(

        Track(
            trackName = "Smells Like Teen Spirit",
            artistName = "Nirvana",
            trackTime = "5:01",
            artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        ),

        Track(
            trackName = "Billie Jean",
            artistName = "Michael Jackson",
            trackTime = "4:35",
            artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        ),

        Track(
            trackName = "Stayin' Alive",
            artistName = "Bee Gees",
            trackTime = "4:10",
            artworkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        ),

        Track(
            trackName = " Whole Lotta Love",
            artistName = "Led Zeppelin",
            trackTime = "5:33",
            artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        ),

        Track(
            trackName = "Sweet Child O'Mine",
            artistName = "Guns N' Roses",
            trackTime = "5:03",
            artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        ),


        )


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