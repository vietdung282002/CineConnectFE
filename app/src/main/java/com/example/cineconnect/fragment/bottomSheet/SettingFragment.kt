package com.example.cineconnect.fragment.bottomSheet

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.cineconnect.R
import com.example.cineconnect.activity.IntroActivity
import com.example.cineconnect.databinding.FragmentSettingBinding
import com.example.cineconnect.model.User
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.utils.SessionManager
import com.example.cineconnect.utils.Utils
import com.example.cineconnect.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.FileDescriptor
import java.io.IOException

class SettingFragment : Fragment() {

    private lateinit var fragmentSettingBinding: FragmentSettingBinding
    private val userViewModel: UserViewModel by viewModels(
        { requireParentFragment().requireParentFragment() }
    )
    private var currentUserId: Int = -1
    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSettingBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        token = "Token " + SessionManager.getToken(requireContext())
        currentUserId = SessionManager.getUserId(requireContext())!!
        return fragmentSettingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSettingBinding.viewModel = userViewModel
        fragmentSettingBinding.lifecycleOwner = viewLifecycleOwner

        fragmentSettingBinding.apply {
            tvChangePassword.setOnClickListener {
                val changePasswordFragment = ChangePasswordFragment()
                parentFragment?.childFragmentManager?.commit {
                    replace(R.id.fragmentContainer, changePasswordFragment)
                    addToBackStack(null)
                }

            }
            cancelBtn.setOnClickListener {
                (parentFragment as? BottomSheetDialogFragment)?.dismiss()
            }
        }


        userViewModel.userResult.observe(viewLifecycleOwner) { response ->
            if (response is BaseResponse.Success) {
                response.data?.let { user -> updateUI(user) }
            }
        }

        userViewModel.logoutResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    logout()
                }

                is BaseResponse.Error -> {
                    processError(response.msg)
                    stopLoading()
                }

                else -> {
                    stopLoading()
                }
            }
        }

        userViewModel.updateUserResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    (parentFragment as? BottomSheetDialogFragment)?.dismiss()
                }

                is BaseResponse.Error -> {
                    processError(response.msg)
                    stopLoading()
                }

                else -> {
                    stopLoading()
                }
            }

        }
    }


    private fun logout() {
        SessionManager.clearData(requireContext())
        val intent = Intent(activity, IntroActivity::class.java)
        startActivity(intent)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        activity?.finish()
    }

    private fun updateUI(user: User) {
        fragmentSettingBinding.apply {
            pickImageBtn.setOnClickListener {
                val options = arrayOf<CharSequence>(
                    getString(R.string.take_photo_button),
                    getString(R.string.choose_from_gallery_button),
                    getString(R.string.cancel_button)
                )
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(getString(R.string.choose_picture_title))
                builder.setItems(options) { dialog, item ->
                    when {
                        options[item] == getString(R.string.take_photo_button) -> {
                            takePhotoWithCamera()
                        }

                        options[item] == getString(R.string.choose_from_gallery_button) -> {
                            pickImageFromGallery()
                        }

                        options[item] == getString(R.string.cancel_button) -> dialog.dismiss()
                    }
                }
                builder.show()
            }
            Glide.with(requireContext()).load(Utils.USER_PROFILE_LINK + user.profilePic)
                .into(imageViewUser)
            tvSignOut.setOnClickListener {
                userViewModel.logout(token!!)
            }

            saveBtn.setOnClickListener {
                userViewModel.updateUser(token!!, currentUserId)
            }

        }
    }

    private fun showLoading() {
        fragmentSettingBinding.progressBarLayout.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        fragmentSettingBinding.progressBarLayout.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private var cameraActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode === AppCompatActivity.RESULT_OK) {
                val imageBitmap = it.data?.extras?.get("data") as Bitmap
                fragmentSettingBinding.imageViewUser.setImageBitmap(imageBitmap)
                userViewModel.uploadImage(imageBitmap)
            }
        }

    private fun takePhotoWithCamera() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            val permission = arrayOf(
                android.Manifest.permission.CAMERA
            )
            requestPermissions(permission, 112)
        } else {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraActivityResultLauncher.launch(cameraIntent)
        }
    }

    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode === AppCompatActivity.RESULT_OK) {
                val imageBitmap = uriToBitmap(it.data?.data!!)
                fragmentSettingBinding.imageViewUser.setImageBitmap(imageBitmap)
                imageBitmap?.let { bitmap ->
                    userViewModel.uploadImage(bitmap)
                }
            }
        }

    private fun pickImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryActivityResultLauncher.launch(galleryIntent)
    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor =
                activity?.contentResolver?.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


}