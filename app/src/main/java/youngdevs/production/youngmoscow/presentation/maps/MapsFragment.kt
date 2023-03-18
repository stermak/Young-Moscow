package youngdevs.production.youngmoscow.presentation.maps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import youngdevs.production.youngmoscow.app.YoungMoscow
import youngdevs.production.youngmoscow.databinding.FragmentMapsBinding
import javax.inject.Inject

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView

    @Inject
    lateinit var mapsViewModelFactory: MapsViewModelFactory

    private lateinit var viewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, mapsViewModelFactory).get(MapsViewModel::class.java)

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
        (activity?.applicationContext as YoungMoscow).appComponent.inject(this)
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
