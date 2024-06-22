package com.example.playlistmaker.creating_and_updating_playlist.ui.fragment

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creating_and_updating_playlist.ui.view_model.CreatingPlaylistViewModel
import com.example.playlistmaker.databinding.FragmentCreatingPlaylistBinding
import com.example.playlistmaker.util.dpToPx
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

open class CreatingPlaylistFragment : Fragment() {

    open val viewModel by viewModel<CreatingPlaylistViewModel>()

    protected lateinit var binding: FragmentCreatingPlaylistBinding

    private val requester = PermissionRequester.instance()

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    protected lateinit var coverIv: ImageView
    protected lateinit var playlistNameEditText: TextInputEditText
    protected lateinit var playlistDescriptionEditText: TextInputEditText
    protected lateinit var createPlaylistButton: AppCompatButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreatingPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coverIv = binding.coverIvCreatingPlaylist
        playlistNameEditText = binding.playlistNameEditText
        playlistDescriptionEditText = binding.playlistDescriptionEditText
        createPlaylistButton = binding.createPlaylistButton

        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()){ //создаем каталог, если он не создан
            filePath.mkdirs()
        }
        viewModel.setFilePath(filePath)

        binding.creatingPlaylistToolbar.setNavigationOnClickListener {
            finishOrShowConfirmDialog()
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finishOrShowConfirmDialog()
                }
            })

        createPlaylistButton.setOnClickListener {
            viewModel.createPlaylist(
                playlistNameEditText.text.toString(),
                playlistDescriptionEditText.text.toString()
            )
            findNavController().navigateUp()
            Toast.makeText(requireContext(),
                (getString(R.string.playlist_created_toast)
                    .format(playlistNameEditText.text.toString())),
                Toast.LENGTH_LONG).show()
        }

        playlistNameEditText.doAfterTextChanged {
            createPlaylistButton.isEnabled = it.toString() != ""
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.finish_creating_playlist))
            .setMessage(getString(R.string.all_unsaved_data_will_be_lost))
            .setNeutralButton(getString(R.string.cancel)) { dialog, which ->
                // ничего не делаем
            }.setPositiveButton(getString(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()
            }

        //регистрируем событие, которое вызывает photo picker
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.setCover(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
        }

        coverIv.setOnClickListener {
            lifecycleScope.launch {
                val storage = if (VERSION.SDK_INT <= 32) READ_EXTERNAL_STORAGE else READ_MEDIA_IMAGES
                requester.request(storage).collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> { // Пользователь дал разрешение, можно продолжать работу
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                        is PermissionResult.Denied.DeniedPermanently -> {
                            Toast.makeText(requireContext(),
                                getString((R.string.toast_permission_read_data)),
                                Toast.LENGTH_LONG).show()
                        }
                        is PermissionResult.Denied.NeedsRationale -> {
                        }
                        is PermissionResult.Cancelled -> {  // Запрос на разрешение отменён
                            return@collect
                        }
                    }
                }
            }
        }

        viewModel.getCoverUriLiveData().observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it)
                .transform(CenterCrop(), RoundedCorners(dpToPx(8F, coverIv.context)))
                .into(coverIv)
        }
    }

    open fun finishOrShowConfirmDialog() {
        if (playlistNameEditText.text.toString().isNotEmpty() ||
            playlistDescriptionEditText.text.toString().isNotEmpty() ||
            viewModel.getCoverUriLiveData().value != null) confirmDialog.show()
        else findNavController().navigateUp()
    }
}