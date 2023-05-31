package com.example.marvel.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.example.marvel.MarvelCharacterDetailActivity
import com.example.marvel.R
import com.example.marvel.entities.MarvelCharacter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class MarvelCharacterAdapter(
    private var marvelCharacters: MutableList<MarvelCharacter>
) :
    RecyclerView.Adapter<MarvelCharacterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val marvelCharacter = marvelCharacters[position]
        holder.bind(marvelCharacter)
    }

    override fun getItemCount(): Int {
        return marvelCharacters.size
    }

    fun addData(newData: List<MarvelCharacter>) {
        val startPosition = marvelCharacters.size
        marvelCharacters.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val cardView: CardView = itemView.findViewById(R.id.characterCardView)
        private val characterImageView: ImageView = itemView.findViewById(R.id.characterImageView)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBarCharacter)


        private val characterNameTextView: TextView =
            itemView.findViewById(R.id.characterNameTextView)
        private val characterDescTextView: TextView =
            itemView.findViewById(R.id.characterDescTextView)

        fun bind(marvelCharacter: MarvelCharacter) {


            val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.item_fade_in)
            itemView.startAnimation(animation)


            characterNameTextView.text = marvelCharacter.name.uppercase()

            characterDescTextView.text = getDescription(marvelCharacter)

            Picasso.get().load(marvelCharacter.thumbnail.getFullPath())
                .into(characterImageView, object : Callback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        progressBar.visibility = View.GONE
                    }
                })


            cardView.setOnClickListener {
                openDetailActivity(itemView, marvelCharacter)

            }
        }

        private fun getDescription(marvelCharacter: MarvelCharacter): CharSequence {
            val maxDescriptionLength = 60
            val description = marvelCharacter.description
            val trimmedDescription = if (description.length > maxDescriptionLength) {
                "${description.substring(0, maxDescriptionLength)}..."
            } else {
                description
            }
            return trimmedDescription
        }

        private fun openDetailActivity(itemView: View, marvelCharacter: MarvelCharacter) {
            val context = itemView.context
            val intent = Intent(context, MarvelCharacterDetailActivity::class.java)

            intent.putExtra("marvelCharacter", marvelCharacter)

            context.startActivity(intent)
            (context as? Activity)?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        }
    }
}

