package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val buttonBack = findViewById<View>(R.id.arrow_back)

        buttonBack.setOnClickListener{
            finish()
        }

        val buttonShare = findViewById<View>(R.id.button_share)

        buttonShare.setOnClickListener{
            shareApp()
        }

        val buttonSupport = findViewById<View>(R.id.button_support)

        buttonSupport.setOnClickListener{
            writeToSupport()
        }

        val buttonUseragreement = findViewById<View>(R.id.button_userAgreement)
        buttonUseragreement.setOnClickListener{
            userAgreement()
        }

    }

    private fun shareApp(){
        val shareText = getString(R.string.share_text)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun writeToSupport(){
        val email = getString(R.string.email)
        val subject = getString(R.string.subject)
        val body = getString(R.string.body)

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT,body)
        }
        startActivity(emailIntent)
    }

    private fun userAgreement(){
        val userAgreementUrl = getString(R.string.userAgreementUrl)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(userAgreementUrl))

        startActivity(browserIntent)
    }





}