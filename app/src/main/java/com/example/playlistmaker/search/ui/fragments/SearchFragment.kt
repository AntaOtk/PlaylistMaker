package com.example.playlistmaker.search.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.activity.AudioPlayer
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.SearchState
import com.example.playlistmaker.search.ui.adapter.SearchAdapter
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()

    companion object {
        const val INPUT_EDIT_TEXT = "INPUT_EDIT_TEXT"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }

    private lateinit var binding: FragmentSearchBinding
    private val tracks = mutableListOf<Track>()
    private val adapter = SearchAdapter(tracks) {
        if (clickDebounce()) {
            viewModel.setTrack(it)
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayer,
                AudioPlayer.createArgs(it)
            )
        }
    }

    private var isClickAllowed = true
    private var inputText: String = ""
    private var simpleTextWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.rvSearch.adapter = adapter
        binding.historySearchList.adapter = adapter


        binding.inputEditText.setOnFocusChangeListener { _, _ ->
            if (inputText.isEmpty()) viewModel.searchHistory()
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clear()
            binding.historySearch.visibility = View.GONE
        }

        binding.clearButtonIcon.setOnClickListener {
            binding.inputEditText.setText("")
            viewModel.searchDebounce("")
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearButtonIcon.windowToken, 0)
        }

        binding.repeatButton.setOnClickListener {
            viewModel.searchDebounce(inputText)
        }

        simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputText = s?.toString() ?: ""
                binding.clearButtonIcon.visibility = clearButtonVisibility(s)
                viewModel.searchDebounce(inputText)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        simpleTextWatcher?.let { binding.inputEditText.addTextChangedListener(it) }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputEditText.text.isNotEmpty()) {
                    viewModel.searchDebounce(inputText)
                }
            }
            false
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        simpleTextWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_EDIT_TEXT, inputText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            inputText = savedInstanceState.getString(INPUT_EDIT_TEXT).toString()
        }
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
        binding.historySearch.visibility = View.VISIBLE
        binding.rvSearch.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        tracks.clear()
        tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    private fun showEmptyHistory() {
        binding.historySearch.visibility = View.GONE
        binding.rvSearch.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.historySearch.visibility = View.GONE
        binding.rvSearch.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        binding.historySearch.visibility = View.GONE
        binding.repeatButton.visibility = View.VISIBLE
        binding.rvSearch.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessageText.text = errorMessage
        binding.placeholderMessageImage.setImageResource(R.drawable.internet_message)

    }

    private fun showEmpty(emptyMessage: String) {
        binding.historySearch.visibility = View.GONE
        binding.repeatButton.visibility = View.GONE
        binding.rvSearch.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessageText.text = emptyMessage
        binding.placeholderMessageImage.setImageResource(R.drawable.search_message)
    }

    private fun showContent(trackList: List<Track>) {
        binding.historySearch.visibility = View.GONE
        binding.rvSearch.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        tracks.clear()
        tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
    }
}
