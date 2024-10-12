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

        val buttonBack = findViewById<View>(R.id.arrow_back)
        buttonBack.setOnClickListener {
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
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                clearTextButton.visibility = clearButtonVisibility(charSequence)
                inputEditTextValue = charSequence.toString()
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