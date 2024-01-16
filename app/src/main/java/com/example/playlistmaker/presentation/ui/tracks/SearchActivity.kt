package com.example.playlistmaker.presentation.ui.tracks

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.presentation.ui.player.AudioPlayerActivity
import com.example.playlistmaker.data.network.ITunesSearchApi
import com.example.playlistmaker.presentation.ui.settings.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener {

    private val retrofit = Retrofit.Builder()
        .baseUrl(I_TUNES_SEARCH_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesSearchService = retrofit.create(ITunesSearchApi::class.java)

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks, this)


    private var searchText = ""
    private var lastQuery = ""

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var editText: EditText
    private lateinit var nothingFound: LinearLayout
    private lateinit var notInternet: LinearLayout
    private lateinit var searchHistory: SearchHistory
    private lateinit var adapterHistory: TrackAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var rvTracks: RecyclerView

    private val searchRunnable = Runnable {
        if (editText.text.isNotEmpty()) {
            findTracks(editText.text.toString())
        }
    }

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        searchHistory = SearchHistory(sharedPreferences)
        val searchHistoryTracks = searchHistory.getHistory()
        adapterHistory = TrackAdapter(searchHistoryTracks, this)

        val backButton = findViewById<ImageButton>(R.id.search_back)
        editText = findViewById(R.id.edit_text)
        nothingFound = findViewById(R.id.nothing_found_layout)
        notInternet = findViewById(R.id.no_internet_layout)
        progressBar = findViewById(R.id.progressBar)
        rvTracks = findViewById(R.id.tracks_recycler_view)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)
        val updateButton = findViewById<Button>(R.id.button_update)
        val llSearchHistory = findViewById<LinearLayout>(R.id.search_history_layout)
        val rvSearchHistory = findViewById<RecyclerView>(R.id.search_history_recycler_view)
        val clearHistoryButton = findViewById<Button>(R.id.button_clear_history)

        rvTracks.adapter = adapter
        rvSearchHistory.adapter = adapterHistory

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            editText.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            adapterHistory.tracks.clear()
            adapterHistory.notifyDataSetChanged()
            llSearchHistory.visibility = View.GONE
        }

        updateButton.setOnClickListener {
            findTracks(lastQuery)
        }

        editText.setOnFocusChangeListener { view, hasFocus ->
            llSearchHistory.visibility = if (hasFocus && editText.text.isEmpty() && adapterHistory.tracks.isNotEmpty()) View.VISIBLE else View.GONE
        }

        editText.doOnTextChanged { s, start, before, count ->
            searchText = editText.text.toString()
            clearButton.visibility = clearButtonVisibility(s)
            if (editText.text.isEmpty()) {
                rvTracks.visibility = View.GONE
                nothingFound.visibility = View.GONE
                notInternet.visibility = View.GONE
            }
            llSearchHistory.visibility = if (editText.hasFocus() && s?.isEmpty() == true && adapterHistory.tracks.isNotEmpty()) View.VISIBLE else View.GONE
            searchDebounce()
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText.text.isNotEmpty()) {
                    findTracks(editText.text.toString())
                }
                true
            }
            false
        }

    }

    override fun onStart() {
        super.onStart()
        editText.requestFocus()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(error: Int) {
        if (error == 1) {
            notInternet.visibility = View.GONE
            nothingFound.visibility = View.VISIBLE
            tracks.clear()
            adapter.notifyDataSetChanged()

        } else if (error == 2){
            nothingFound.visibility = View.GONE
            notInternet.visibility = View.VISIBLE
            tracks.clear()
            adapter.notifyDataSetChanged()
        } else {
            nothingFound.visibility = View.GONE
            notInternet.visibility = View.GONE
        }
    }

    private fun findTracks(query: String) {
        nothingFound.visibility = View.GONE
        notInternet.visibility = View.GONE
        rvTracks.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        iTunesSearchService.search(query).enqueue(object :
            Callback<TracksSearchResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<TracksSearchResponse>,
                                    response: Response<TracksSearchResponse>
            ) {
                progressBar.visibility = View.GONE // Прячем ProgressBar после успешного выполнения запроса
                if (response.code() == 200) {
                    tracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        rvTracks.visibility = View.VISIBLE
                        tracks.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()
                    }
                    if (tracks.isEmpty()) {
                        showMessage(NOTHING_FOUND)
                    } else {
                        showMessage(0)
                    }
                } else {
                    lastQuery = query
                    showMessage(NO_INTERNET)
                }
            }

            override fun onFailure(call: Call<TracksSearchResponse>, t: Throwable) {
                progressBar.visibility = View.GONE // Прячем ProgressBar после выполнения запроса с ошибкой
                lastQuery = query
                showMessage(NO_INTERNET)
            }

        })
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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
    override fun onClick(track: Track) {
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
        const val I_TUNES_SEARCH_BASE_URL = "https://itunes.apple.com"
        private const val NOTHING_FOUND = 1
        private const val NO_INTERNET = 2
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
