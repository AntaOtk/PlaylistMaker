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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.main.ui.MainActivityViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.SearchState
import com.example.playlistmaker.search.ui.adapter.SearchAdapter
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.search.util.debounce
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()
    private val hostViewModel by activityViewModel<MainActivityViewModel>()

    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private var _binding: FragmentSearchBinding? =null
    private val binding get() = _binding!!
    private val tracks = mutableListOf<Track>()
    private val adapter = SearchAdapter(tracks) { track ->
        onTrackClickDebounce(track)
    }

    private var inputText: String = ""
    private var simpleTextWatcher: TextWatcher? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSearch.adapter = adapter
        binding.historySearchList.adapter = adapter

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            viewModel.setTrack(track)
            hostViewModel.setCurrentTrack(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayer
            )
        }
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
                binding.clearButtonIcon.isVisible = !s.isNullOrEmpty()
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
        _binding = null
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

    private fun showHistory(trackList: List<Track>) {
        binding.historySearch.isVisible = true
        binding.rvSearch.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.progressBar.isVisible = false
        tracks.clear()
        tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    private fun showEmptyHistory() {
        binding.historySearch.isVisible = false
        binding.rvSearch.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun showLoading() {
        binding.historySearch.isVisible = false
        binding.rvSearch.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showError(errorMessage: String) {
        binding.historySearch.isVisible = false
        binding.repeatButton.isVisible = true
        binding.rvSearch.isVisible = false
        binding.placeholderMessage.isVisible = true
        binding.progressBar.isVisible = false
        binding.placeholderMessageText.text = errorMessage
        binding.placeholderMessageImage.setImageResource(R.drawable.internet_message)

    }

    private fun showEmpty(emptyMessage: String) {
        binding.historySearch.isVisible = false
        binding.repeatButton.isVisible = false
        binding.rvSearch.isVisible = false
        binding.placeholderMessage.isVisible = true
        binding.progressBar.isVisible = false
        binding.placeholderMessageText.text = emptyMessage
        binding.placeholderMessageImage.setImageResource(R.drawable.search_message)
    }

    private fun showContent(trackList: List<Track>) {
        binding.historySearch.isVisible = false
        binding.rvSearch.isVisible = true
        binding.placeholderMessage.isVisible = false
        binding.progressBar.isVisible = false
        tracks.clear()
        tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val INPUT_EDIT_TEXT = "INPUT_EDIT_TEXT"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
    }
}
