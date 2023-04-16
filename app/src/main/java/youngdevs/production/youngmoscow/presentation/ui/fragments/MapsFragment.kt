package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.databinding.FragmentMapsBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.MapsViewModel
import javax.inject.Inject

// Фрагмент, отображающий карту и текущее местоположение пользователя
@AndroidEntryPoint // аннотация для использования Hilt DI
class MapsFragment : Fragment() {

    // Поле для привязки View Binding
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    // ViewModel для работы с данными
    private val viewModel: MapsViewModel by viewModels()

    @Inject
    lateinit var googleMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Инициализация View Binding и возвращение корневого View макета фрагмента
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация MapView

        val fab: FloatingActionButton = binding.fab
        fab.setOnClickListener {
            //moveToCurrentLocation()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
