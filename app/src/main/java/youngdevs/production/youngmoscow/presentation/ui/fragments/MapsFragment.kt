package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
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
    private val locationRequestCode = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root // возвращение корневого View макета фрагмента
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView

        if (viewModel.hasLocationPermission()) {
            moveToCurrentLocation()
        } else {
            viewModel.requestLocationPermission(requireActivity())
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                moveToCurrentLocation()
            } else {
                // Разрешение не предоставлено, показать сообщение об ошибке
            }
        }
    }

    private fun moveToCurrentLocation() {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (viewModel.hasLocationPermission()) {
            try {
                val locationProvider = LocationManager.GPS_PROVIDER
                val lastKnownLocation = locationManager.getLastKnownLocation(locationProvider)
                if (lastKnownLocation != null) {
                    mapView.map.move(
                        CameraPosition(
                            Point(lastKnownLocation.latitude, lastKnownLocation.longitude),
                            14.0f, 0.0f, 0.0f
                        )
                    )
                }
            } catch (e: SecurityException) {
                // Разрешение не предоставлено или отозвано, показать сообщение об ошибке или выполнить другое действие
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}