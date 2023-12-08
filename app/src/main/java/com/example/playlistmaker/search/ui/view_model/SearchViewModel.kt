package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var latestSearchText: String? = null

    private var searchJob: Job? = null


    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText


        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            search(changedText)
        }
    }

    private val tracks = ArrayList<Track>()

    fun searchHistory() {
        val history = tracksInteractor.getTrackList()
        if (history.isNotEmpty())
            renderState(
                SearchState.EmptyInput(
                    history
                )
            ) else renderState(
            SearchState.AllEmpty
        )
    }

    fun setTrack(track: Track) {
        tracksInteractor.setTrack(track)
    }

    fun clear() {
        tracksInteractor.clear()
    }


    private fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                SearchState.Loading
            )
            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        } else {
            searchHistory()
        }
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    private fun processResult(foundTrack: List<Track>?, errorMessage: String?) {
        if (foundTrack != null) {
            tracks.clear()
            tracks.addAll(foundTrack)
        }
        when {
            errorMessage != null -> {
                renderState(
                    SearchState.Error(
                        errorMessage
                    )
                )
            }

            tracks.isEmpty() -> {
                renderState(
                    SearchState.Empty(
                        tracksInteractor.getEmptyMessage(),
                    )
                )
            }

            else -> {
                renderState(
                    SearchState.Content(
                        tracks
                    )
                )
            }
        }
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}
