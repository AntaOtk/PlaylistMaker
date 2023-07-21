package com.example.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.SearchState

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }


    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var latestSearchText: String? = null
    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { search(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY_MILLIS
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
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
            tracksInteractor.searchTracks(
                newSearchText
            ) { foundTrack, errorMessage ->
                handler.post {
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
            }
        } else {
            searchHistory()
        }
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }
}
