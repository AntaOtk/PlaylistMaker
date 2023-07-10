package com.example.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.AudioPlayer
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.SearchAdapter
import com.example.playlistmaker.search.ui.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {

    companion object {
        const val INPUT_EDIT_TEXT = "INPUT_EDIT_TEXT"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        fun startActivity(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivitySearchBinding


    private lateinit var inputEditText: EditText
    private lateinit var placeholderMessage: LinearLayout
    private lateinit var searchList: RecyclerView
    private lateinit var hintMessage: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var textMessage: TextView
    private lateinit var imageMessage: ImageView
    private lateinit var buttonRepeat: Button
    private lateinit var viewModel: SearchViewModel

    private val tracks = mutableListOf<Track>()
    private val adapter = SearchAdapter(tracks) {
        if (clickDebounce()) {
            viewModel.setTrack(it)
            AudioPlayer.startActivity(this, it)
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private var inputText: String = ""
    private var simpleTextWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        binding = ActivitySearchBinding.inflate(layoutInflater)

        inputEditText = findViewById(R.id.inputEditText)
        searchList = findViewById(R.id.rvSearch)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        progressBar = findViewById(R.id.progressBar)
        textMessage = findViewById(R.id.placeholderMessageText)
        imageMessage = findViewById(R.id.placeholderMessageImage)
        buttonRepeat = findViewById(R.id.repeatButton)

        val imageBack = findViewById<ImageView>(R.id.backToMainActivity)
        imageBack.setOnClickListener { finish() }

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearButtonIcon)
        val historyList = findViewById<RecyclerView>(R.id.historySearchList)
        hintMessage = findViewById(R.id.historySearch)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)

        searchList.adapter = adapter
        historyList.adapter = adapter


        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            viewModel.searchDebounce(inputText)
        }

        clearHistoryButton.setOnClickListener {
            viewModel.clear()
            hintMessage.visibility = View.GONE
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            viewModel.searchDebounce("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.getWindowToken(), 0)
        }


        buttonRepeat.setOnClickListener {
            viewModel.searchDebounce(inputText)
        }

        simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                viewModel.searchDebounce(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        simpleTextWatcher?.let { inputEditText.addTextChangedListener(it) }

        viewModel.observeState().observe(this) {
            render(it)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    viewModel.searchDebounce(inputText)
                }
            }
            false
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        simpleTextWatcher?.let { inputEditText.removeTextChangedListener(it) }
    }


    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(
                { isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY_MILLIS
            )
        }
        return current
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_EDIT_TEXT, inputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputText = savedInstanceState.getString(INPUT_EDIT_TEXT).toString()
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Error -> showError(state.errorMessage)
            is SearchState.Empty -> showEmpty(state.message)
            is SearchState.EmptyInput -> showHistory(state.tracks)
            is SearchState.AllEmpty -> showEmptyHistory()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showHistory(trackList: List<Track>) {
        hintMessage.visibility = View.VISIBLE
        buttonRepeat.visibility = View.GONE
        searchList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE
        tracks.clear()
        tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    private fun showEmptyHistory() {
        hintMessage.visibility = View.GONE
        buttonRepeat.visibility = View.GONE
        searchList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        hintMessage.visibility = View.GONE
        buttonRepeat.visibility = View.GONE
        searchList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        hintMessage.visibility = View.GONE
        buttonRepeat.visibility = View.VISIBLE
        searchList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        textMessage.text = errorMessage
        imageMessage.setImageResource(R.drawable.internet_message)

    }

    private fun showEmpty(emptyMessage: String) {
        hintMessage.visibility = View.GONE
        buttonRepeat.visibility = View.GONE
        searchList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        textMessage.text = emptyMessage
        imageMessage.setImageResource(R.drawable.search_message)
    }

    private fun showContent(trackList: List<Track>) {
        hintMessage.visibility = View.GONE
        buttonRepeat.visibility = View.GONE
        searchList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE
        tracks.clear()
        tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

}

