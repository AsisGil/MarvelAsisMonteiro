package com.example.marvel

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.adapter.MarvelCharacterAdapter
import com.example.marvel.entities.MarvelCharacter
import com.google.gson.Gson
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    private val privateKey = "7543cd04b95dfa48210cc7af1a62e80ec206b992"
    private val publicKey = "f58c3e4b2c624592e6d97bc0b976bab5"
    private lateinit var progressBar: ProgressBar

    private lateinit var marvelCharacterRecyclerView: RecyclerView
    private lateinit var marvelCharacterAdapter: MarvelCharacterAdapter
    private val marvelCharactersList: MutableList<MarvelCharacter> = mutableListOf()

    private val visible = 5

    private var offset = 0
    private var isLoading = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        marvelCharacterRecyclerView = findViewById(R.id.marvelCharacterRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        setupRecyclerView()
        getMarvelCharactersFromApi(offset)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        marvelCharacterRecyclerView.layoutManager = layoutManager
        marvelCharacterAdapter =
            MarvelCharacterAdapter(marvelCharactersList)
        marvelCharacterRecyclerView.adapter = marvelCharacterAdapter

        marvelCharacterRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount <= (lastVisibleItem + visible)) {
                    if (!isLoading) {
                        offset += 50
                        getMarvelCharactersFromApi(offset)
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })

        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                marvelCharactersList.removeAt(position)
                marvelCharacterAdapter.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(marvelCharacterRecyclerView)


    }

    private fun generateHash(ts: String): String {
        val input = "$ts$privateKey$publicKey"
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    private fun getMarvelCharactersFromApi(offset: Number) {
        isLoading = true
        val ts = System.currentTimeMillis().toString()
        val hash = generateHash(ts)
        val url =
            "https://gateway.marvel.com:443/v1/public/characters?limit=50?&offset=$offset&apikey=$publicKey&ts=$ts&hash=$hash"

        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val request = Request.Builder()
            .url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Error Api de Marvel: ", e.toString())
                runOnUiThread {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val gson = Gson()
                val marvelCharactersResponse =
                    gson.fromJson(responseData, MarvelCharactersResponse::class.java)
                if (marvelCharactersResponse != null) {
                    runOnUiThread {
                        marvelCharacterAdapter.addData(marvelCharactersResponse.data.results)
                        isLoading = false
                        progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        })
    }
}
