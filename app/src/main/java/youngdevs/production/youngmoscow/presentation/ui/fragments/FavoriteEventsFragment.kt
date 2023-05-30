package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.databinding.FragmentFavoriteEventsBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.FavoriteEventsViewModel

@AndroidEntryPoint
class FavoriteEventsFragment : Fragment() {

    private var _binding: FragmentFavoriteEventsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteEventsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

