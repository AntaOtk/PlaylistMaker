package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.searchlist.APIsearch
import com.example.playlistmaker.searchlist.SearchAdapter
import com.example.playlistmaker.searchlist.Track
import com.example.playlistmaker.searchlist.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val INPUT_EDIT_TEXT = "INPUT_EDIT_TEXT"
    }

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit =
        Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val itunesService = retrofit.create(APIsearch::class.java)
    private var text: String = ""
    private lateinit var inputEditText: EditText
    private lateinit var placeholderMessage: LinearLayout
    private lateinit var searchList: RecyclerView

    private val tracks = ArrayList<Track>()
    private val adapter = SearchAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById(R.id.inputEditText)
        searchList = findViewById(R.id.rvSearch)
        placeholderMessage = findViewById(R.id.placeholderMessage)

        val imageBack = findViewById<ImageView>(R.id.backToMainActivity)
        imageBack.setOnClickListener { finish() }

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearButtonIcon)
        val searchList = findViewById<RecyclerView>(R.id.rvSearch)
        searchList.adapter = adapter


        clearButton.setOnClickListener {
            inputEditText.setText("")
        }

        val buttonRepeat = findViewById<Button>(R.id.repeatButton)
        buttonRepeat.setOnClickListener {
            search()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = inputEditText.text.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }

            private fun clearButtonVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    search()
                }
                true
            }
            false
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)
    }



    private fun search() {
        itunesService.search(inputEditText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>, response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            showMessage(getString(R.string.nothing_found), 1)
                        } else {
                            showMessage("", 0)
                        }
                    } else {
                        showMessage(
                            getString(R.string.no_interrnet_conection), 2
                        )
                    }

                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showMessage(getString(R.string.no_interrnet_conection), 2)
                }

            })

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_EDIT_TEXT, text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString(INPUT_EDIT_TEXT).toString()

    }

    private fun showMessage(text: String, type: Int) {
        val buttonRepeat = findViewById<Button>(R.id.repeatButton)
        buttonRepeat.visibility = View.GONE
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            val textMessage = findViewById<TextView>(R.id.placeholderMessageText)
            val imageMessage = findViewById<ImageView>(R.id.placeholderMessageImage)
            tracks.clear()
            adapter.notifyDataSetChanged()
            when (type){
                1 -> {
                    textMessage.text = text
                    imageMessage.setImageResource(R.drawable.search_message)
                }
                2 -> {
                    textMessage.text = text
                    imageMessage.setImageResource(R.drawable.internet_message)
                    buttonRepeat.visibility = View.VISIBLE
                }
            }
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }

}
