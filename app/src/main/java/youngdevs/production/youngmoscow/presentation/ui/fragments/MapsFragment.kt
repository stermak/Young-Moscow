package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.databinding.FragmentMapsBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.MapsViewModel

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView

    private val viewModel: MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация MapView и установка начальной точки камеры на карте
        mapView = binding.mapView
        mapView.map.move(
            CameraPosition(
                Point(55.751244, 37.618423),
                14.0f, 0.0f, 0.0f
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root // возвращение корневого View макета фрагмента
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop() // остановка MapView
        MapKitFactory.getInstance().onStop() // остановка MapKit
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart() // запуск MapView
        MapKitFactory.getInstance().onStart() // запуск MapKit
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // очистка переменной _binding для избежания утечек памяти
    }
}
