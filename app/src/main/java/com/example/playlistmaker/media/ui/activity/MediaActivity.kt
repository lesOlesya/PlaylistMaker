package com.example.playlistmaker.media.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.media.ui.adapter.MediaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mediaBack.setOnClickListener {
            finish()
        }

//        val poster = intent.getStringExtra("poster") ?: ""
//        val movieId = intent.getStringExtra("id") ?: ""
//
//        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager,
//            lifecycle, poster, movieId)
        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager,
            lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}