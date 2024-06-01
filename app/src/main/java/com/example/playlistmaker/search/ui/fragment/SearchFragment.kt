package com.example.playlistmaker.search.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksState
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.search.ui.view_model.TracksSearchViewModel
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackAdapter.TrackClickListener {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModel<TracksSearchViewModel>()

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private val adapter = TrackAdapter(this)

    private var textWatcher: TextWatcher? = null

    private lateinit var editText: EditText
    private lateinit var nothingFound: LinearLayout
    private lateinit var noInternet: LinearLayout
    private lateinit var adapterHistory: TrackAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var rvTracks: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            viewModel.addTrackToSearchHistory(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerActivity,
                AudioPlayerActivity.createArgs(track.trackId)
            )
        }

        adapterHistory = TrackAdapter(this)
        adapterHistory.tracks = viewModel.getSearchHistoryLiveData().value!!

        editText = binding.editText
        nothingFound = binding.nothingFoundLayout
        noInternet = binding.noInternetLayout
        progressBar = binding.progressBar
        rvTracks = binding.tracksRecyclerView
        val clearButton = binding.clearIcon
        val llSearchHistory = binding.searchHistoryLayout
        val rvSearchHistory = binding.searchHistoryRecyclerView

        rvTracks.adapter = adapter
        rvSearchHistory.adapter = adapterHistory

        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
            llSearchHistory.visibility = View.GONE
        }

        binding.buttonUpdate.setOnClickListener {
            viewModel.updateDebounce()
        }

        clearButton.setOnClickListener {
            editText.setText("")
            adapter.notifyDataSetChanged()
        }

        editText.setOnFocusChangeListener { view, hasFocus ->
            llSearchHistory.visibility =
                if (hasFocus && editText.text.isEmpty() && adapterHistory.tracks.isNotEmpty()) View.VISIBLE else View.GONE
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if (editText.text.isEmpty()) {
                    rvTracks.visibility = View.GONE
                    messageVisibility(noInternetIsVisible = false, nothingFoundIsVisible = false)
                }
                llSearchHistory.visibility =
                    if (editText.hasFocus() && s?.isEmpty() == true && adapterHistory.tracks.isNotEmpty()) View.VISIBLE else View.GONE
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { editText.addTextChangedListener(it) }

        editText.setOnEditorActionListener { _, actionId, _ -> //enter на клаве
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(
                    changedText = editText.text.toString()
                )
                true
            }
            false
        }

        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.getSearchHistoryLiveData().observe(viewLifecycleOwner) { searchHistory ->
            adapterHistory.tracks = searchHistory
            adapterHistory.notifyDataSetChanged()
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
        if (editText.text.isNotEmpty()) rvTracks.visibility = View.VISIBLE
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onTrackClick(track: Track) {
        onTrackClickDebounce(track)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}