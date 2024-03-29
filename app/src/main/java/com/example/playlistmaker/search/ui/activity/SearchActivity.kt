package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.creator.Creator.provideSearchHistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.ui.models.TracksState
import com.example.playlistmaker.search.ui.view_model.TracksSearchViewModel

class SearchActivity : AppCompatActivity(), TrackAdapter.TrackClickListener {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var tracksSearchViewModel: TracksSearchViewModel

    private var isClickAllowed = true

    private val adapter = TrackAdapter(this)

    private var searchText = ""
    private var textWatcher: TextWatcher? = null

    private lateinit var editText: EditText
    private lateinit var nothingFound: LinearLayout
    private lateinit var noInternet: LinearLayout
    private lateinit var adapterHistory: TrackAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var rvTracks: RecyclerView

    private val searchHistory by lazy { provideSearchHistoryInteractor(applicationContext) }

    private val handler = Handler(Looper.getMainLooper())

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tracksSearchViewModel = ViewModelProvider(this, TracksSearchViewModel.getViewModelFactory())[TracksSearchViewModel::class.java]

        val searchHistoryTracks = searchHistory.getHistory()
        adapterHistory = TrackAdapter(this)
        adapterHistory.tracks = searchHistoryTracks

        editText = binding.editText
        nothingFound = binding.nothingFoundLayout
        noInternet = binding.noInternetLayout
        progressBar = binding.progressBar
        rvTracks = binding.tracksRecyclerView
        val clearButton = binding.clearIcon
        val updateButton = binding.buttonUpdate
        val llSearchHistory = binding.searchHistoryLayout
        val rvSearchHistory = binding.searchHistoryRecyclerView
        val clearHistoryButton = binding.buttonClearHistory

        rvTracks.adapter = adapter
        rvSearchHistory.adapter = adapterHistory

        binding.searchBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            editText.setText("")
            //tracks.clear() 11111111111111111111111111111111111111
            adapter.notifyDataSetChanged()
        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            adapterHistory.tracks.clear()
            adapterHistory.notifyDataSetChanged()
            llSearchHistory.visibility = View.GONE
        }

        updateButton.setOnClickListener {
            tracksSearchViewModel.searchDebounce(
                changedText = editText.text.toString()
            )
        }

        editText.setOnFocusChangeListener { view, hasFocus ->
            llSearchHistory.visibility =
                if (hasFocus && editText.text.isEmpty() && adapterHistory.tracks.isNotEmpty()) View.VISIBLE else View.GONE
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = editText.text.toString()
                clearButton.visibility = clearButtonVisibility(s)
                if (editText.text.isEmpty()) {
                    rvTracks.visibility = View.GONE
                    nothingFound.visibility = View.GONE
                    noInternet.visibility = View.GONE
                }
                llSearchHistory.visibility =
                    if (editText.hasFocus() && s?.isEmpty() == true && adapterHistory.tracks.isNotEmpty()) View.VISIBLE else View.GONE
                tracksSearchViewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { editText.addTextChangedListener(it) }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tracksSearchViewModel.searchDebounce(
                    changedText = editText.text.toString()
                )
                true
            }
            false
        }

        tracksSearchViewModel.observeState().observe(this) {
            render(it)
        }
    }

    override fun onStart() {
        super.onStart()
        editText.requestFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { editText.removeTextChangedListener(it) }
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Error -> showError(state.errorCode)
            is TracksState.Empty -> showEmpty(state.code)
        }
    }

    private fun showLoading() {
        rvTracks.visibility = View.GONE
        messageVisibility(noInternetIsVisible = false, nothingFoundIsVisible = false)
        progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorCode: Int) {
        rvTracks.visibility = View.GONE
        when (errorCode) {
            0, 400 -> messageVisibility(noInternetIsVisible = false, nothingFoundIsVisible = true)
            -1 -> messageVisibility(noInternetIsVisible = true, nothingFoundIsVisible = false)
            else -> messageVisibility(noInternetIsVisible = false, nothingFoundIsVisible = false)
        }
        progressBar.visibility = View.GONE
    }

    private fun showEmpty(emptyCode: Int) {
        showError(emptyCode)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(tracks: List<Track>) {
        rvTracks.visibility = View.VISIBLE
        messageVisibility(noInternetIsVisible = false, nothingFoundIsVisible = false)
        progressBar.visibility = View.GONE

        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun messageVisibility(noInternetIsVisible: Boolean, nothingFoundIsVisible: Boolean) {
        noInternet.visibility = if (noInternetIsVisible) View.VISIBLE else View.GONE
        nothingFound.visibility = if (nothingFoundIsVisible) View.VISIBLE else View.GONE
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onSaveInstanceState(saveInstanceState: Bundle) {
        saveInstanceState.putString(SEARCH_TEXT, searchText)
        super.onSaveInstanceState(saveInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        editText.setText(searchText)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            searchHistory.addTrack(track)
            adapterHistory.tracks = searchHistory.getHistory()
            adapterHistory.notifyDataSetChanged()
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra("TrackId", track.trackId)
            startActivity(intent)
        }
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
