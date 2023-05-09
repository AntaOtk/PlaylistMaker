package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
    private val searchHistory = SearchHistory()

    private val itunesService = retrofit.create(APIsearch::class.java)
    private var text: String = ""
    private lateinit var inputEditText: EditText
    private lateinit var placeholderMessage: LinearLayout
    private lateinit var searchList: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences

    private val tracks = ArrayList<Track>()
    private val adapter = SearchAdapter(tracks) {
        searchHistory.setTrack(it, sharedPreferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPreferences = getSharedPreferences(PRACTICUM_PREFERENCES, MODE_PRIVATE)


        inputEditText = findViewById(R.id.inputEditText)
        searchList = findViewById(R.id.rvSearch)
        placeholderMessage = findViewById(R.id.placeholderMessage)

        val imageBack = findViewById<ImageView>(R.id.backToMainActivity)
        imageBack.setOnClickListener { finish() }

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearButtonIcon)
        val historyList = findViewById<RecyclerView>(R.id.historySearchList)
        val hintMessage = findViewById<LinearLayout>(R.id.historySearch)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
        searchList.adapter = adapter


        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            hintMessage.visibility =
                if (hasFocus && inputEditText.text.isEmpty() && !searchHistory.read(sharedPreferences).isEmpty()) View.VISIBLE else View.GONE
            historyList.adapter = SearchAdapter(searchHistory.read(sharedPreferences)) {}

        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clear(sharedPreferences)
            hintMessage.visibility = View.GONE
        }



        clearButton.setOnClickListener {
            inputEditText.setText("")
            showMessage(InputStatus.SUCCESS)
            tracks.clear()
            adapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.getWindowToken(), 0)
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

                    if (inputEditText.hasFocus() && s?.isEmpty() == true) {
                        showMessage(InputStatus.SUCCESS)
                        if (!searchHistory.read(sharedPreferences).isEmpty()) hintMessage.visibility =View.VISIBLE
                    } else{
                        hintMessage.visibility =View.GONE
                    }
                historyList.adapter = SearchAdapter(searchHistory.read(sharedPreferences)) {}
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
                            showMessage(InputStatus.SUCCESS)
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            showMessage(InputStatus.EMPTY)
                        }
                    } else {
                        showMessage(
                            InputStatus.ERROR
                        )
                    }

                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showMessage(InputStatus.ERROR)
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

    private fun showMessage(status: InputStatus) {
        val buttonRepeat = findViewById<Button>(R.id.repeatButton)
        buttonRepeat.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        val textMessage = findViewById<TextView>(R.id.placeholderMessageText)
        val imageMessage = findViewById<ImageView>(R.id.placeholderMessageImage)
        tracks.clear()
        adapter.notifyDataSetChanged()
        when (status) {
            InputStatus.SUCCESS -> {
                placeholderMessage.visibility = View.GONE
            }
            InputStatus.EMPTY -> {
                textMessage.text = getString(R.string.nothing_found)
                imageMessage.setImageResource(R.drawable.search_message)
            }
            InputStatus.ERROR -> {
                textMessage.text = getString(R.string.no_interrnet_conection)
                imageMessage.setImageResource(R.drawable.internet_message)
                buttonRepeat.visibility = View.VISIBLE
            }
        }

    }

}

