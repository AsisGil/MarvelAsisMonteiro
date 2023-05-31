package com.example.marvel

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.marvel.entities.MarvelCharacter
import com.squareup.picasso.Picasso

class MarvelCharacterDetailActivity : AppCompatActivity() {


    private lateinit var characterImageView: ImageView
    private lateinit var characterNameTextView: TextView
    private lateinit var characterDescriptionTextView: TextView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        characterImageView = findViewById(R.id.characterImageView)
        characterNameTextView = findViewById(R.id.characterNameTextViewDet)
        characterDescriptionTextView = findViewById(R.id.characterDescTextViewDet)

        val marvelCharacter = intent.getParcelableExtra(
            "marvelCharacter",
            MarvelCharacter::class.java
        )
        title = marvelCharacter?.name ?: "Character detail"
        if (marvelCharacter != null) {
            characterNameTextView.text = marvelCharacter.name
            characterDescriptionTextView.text = marvelCharacter.description
            if (marvelCharacter.description.isBlank()) {
                characterDescriptionTextView.text =
                    getString(R.string.marvel_character_without_description)
            }
            Picasso.get().load(marvelCharacter.thumbnail.getFullPath()).into(characterImageView)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

}
