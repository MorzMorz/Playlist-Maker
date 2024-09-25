package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)

    buttonSearch.setOnClickListener{
        Toast.makeText(this@MainActivity, "Нажали на кнопку 'Поиск'!", Toast.LENGTH_SHORT).show()
    }
        val buttonMediateka = findViewById<Button>(R.id.button_mediateka)

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на кнопку 'Медиатека'!", Toast.LENGTH_SHORT).show()
            }
        }
        buttonMediateka.setOnClickListener(buttonClickListener)

        val buttonSettings = findViewById<Button>(R.id.button_settings)

        buttonSettings.setOnClickListener{
            Toast.makeText(this@MainActivity, "Нажали на кнопку 'Настройки'!", Toast.LENGTH_SHORT).show()
        }


    }





    }



