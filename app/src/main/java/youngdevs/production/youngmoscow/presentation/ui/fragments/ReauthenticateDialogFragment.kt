package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.databinding.DialogReauthenticateBinding

@AndroidEntryPoint
class ReauthenticateDialogFragment : DialogFragment() {

    interface ReauthenticateListener {
        fun onReauthenticate(password: String)
    }

    private lateinit var listener: ReauthenticateListener
    private lateinit var binding: DialogReauthenticateBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding =
            DialogReauthenticateBinding.inflate(
                requireActivity().layoutInflater
            )

        return AlertDialog.Builder(requireContext())
            .setTitle("Введите текущий пароль")
            .setView(binding.root)
            .setPositiveButton("Подтвердить") { _, _ ->
                val password = binding.passwordEditText.text.toString()
                listener.onReauthenticate(password)
            }
            .setNegativeButton("Отмена") { _, _ -> }
            .create()
    }

    fun setReauthenticateListener(listener: ReauthenticateListener) {
        this.listener = listener
    }
}
