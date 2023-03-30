package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.databinding.FragmentSightseeingsBinding
import youngdevs.production.youngmoscow.presentation.ui.adapter.SightseeingsAdapter
import youngdevs.production.youngmoscow.presentation.viewmodel.SightseeingsViewModel

@AndroidEntryPoint
class SightseeingsFragment : Fragment() {

    private val viewModel: SightseeingsViewModel by viewModels()

    private var _binding: FragmentSightseeingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sightseeingsAdapter: SightseeingsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSightseeingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sightseeingsAdapter = SightseeingsAdapter(viewLifecycleOwner.lifecycleScope)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = sightseeingsAdapter

        viewModel.sightseeings.observe(viewLifecycleOwner) { sightseeings ->
            sightseeingsAdapter.submitList(sightseeings)
        }

        viewModel.loadSightseeings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
