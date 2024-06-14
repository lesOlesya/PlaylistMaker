package com.example.playlistmaker.creating_playlist.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.FragmentCreatingPlaylistBinding
import com.example.playlistmaker.util.dpToPx
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class CreatingPlaylistFragment : Fragment() {

    private val viewModel by viewModel<CreatingPlaylistViewModel>()

    private lateinit var binding: FragmentCreatingPlaylistBinding

    private val requester = PermissionRequester.instance()

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private lateinit var playlistNameEditText: TextInputEditText
    private lateinit var playlistDescriptionEditText: TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreatingPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coverIv = binding.coverIvCreatingPlaylist
        val createPlaylistButton = binding.createPlaylistButton
        playlistNameEditText = binding.playlistNameEditText
        playlistDescriptionEditText = binding.playlistDescriptionEditText

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
                ("Плейлист ${playlistNameEditText.text.toString()} создан"),
                Toast.LENGTH_LONG).show()
        }

        playlistNameEditText.doAfterTextChanged {
            createPlaylistButton.isEnabled = it.toString() != ""
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setPositiveButton("Завершить") { dialog, which ->
                findNavController().navigateUp()
            }

        //регистрируем событие, которое вызывает photo picker
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(this)
                        .load(uri)
                        .transform(CenterCrop(), RoundedCorners(dpToPx(8F, coverIv.context)))
                        .into(coverIv)
                    viewModel.uri = uri
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
                                "Нужно разрешение на доступ к файлам устройста",
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
    }

    private fun finishOrShowConfirmDialog() {
        if (playlistNameEditText.text.toString().isNotEmpty() ||
            playlistDescriptionEditText.text.toString().isNotEmpty() ||
            viewModel.uri != null) confirmDialog.show()
        else findNavController().navigateUp()
    }
}