package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.FragmentMapsBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.MapsViewModel

@AndroidEntryPoint
class MapsFragment : Fragment() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var _binding: FragmentMapsBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: MapsViewModel by viewModels()
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient:
            FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private lateinit var geoApiContext: GeoApiContext

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentMapsBinding.inflate(layoutInflater, container, false)
        Places.initialize(
            requireContext(),
            getString(R.string.google_maps_key)
        )
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(
                requireContext()
            )
        placesClient = Places.createClient(requireContext())
        geoApiContext =
            GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_key))
                .build()
        return binding.root
    }

    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        viewModel.locationPermission.observe(viewLifecycleOwner) {
                isGranted ->
            if (isGranted) {
                if (hasLocationPermission()) {
                    googleMap.isMyLocationEnabled = true
                    googleMap.uiSettings.isMyLocationButtonEnabled = true
                    getLastKnownLocation {}
                }
            }

            googleMap.setOnMapClickListener { latLng ->
                // Добавление маркера и прокладка маршрута
                addMarkerAndRoute(latLng)
            }
        }
    }

    private fun addMarkerAndRoute(latLng: LatLng) {
        googleMap
            .clear() // Очистка карты от предыдущих маркеров и маршрутов
        val marker = MarkerOptions().position(latLng).title("Маркер")
        googleMap.addMarker(marker)

        getLastKnownLocation { location ->
            if (location != null) {
                val currentLatLng =
                    LatLng(location.latitude, location.longitude)
                getRoute(currentLatLng, latLng)
            }
        }
    }

    private fun getLastKnownLocation(callback: (Location?) -> Unit) {
        if (
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                location ->
            callback(location)
        }
    }

    private fun getRoute(origin: LatLng, destination: LatLng) {
        val directionsApiRequest =
            DirectionsApi.newRequest(geoApiContext)
                .mode(
                    TravelMode.WALKING
                ) // Выберите режим передвижения (например, WALKING, DRIVING, и т.д.)
                .origin(
                    com.google.maps.model.LatLng(
                        origin.latitude,
                        origin.longitude
                    )
                )
                .destination(
                    com.google.maps.model.LatLng(
                        destination.latitude,
                        destination.longitude
                    )
                )

        directionsApiRequest.setCallback(
            object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult) {
                    val path: MutableList<List<LatLng>> = ArrayList()
                    if (
                        result.routes.isNotEmpty() &&
                        result.routes[0].legs.isNotEmpty()
                    ) {
                        for (route in result.routes) {
                            for (leg in route.legs) {
                                for (step in leg.steps) {
                                    path.add(
                                        PolyUtil.decode(
                                            step.polyline.encodedPath
                                        )
                                    )
                                }
                            }
                        }
                    }

                    if (path.isNotEmpty()) {
                        activity?.runOnUiThread {
                            val polylineOptions =
                                PolylineOptions()
                                    .addAll(path.flatten())
                                    .color(Color.BLUE)
                                    .width(10f)

                            googleMap.addPolyline(polylineOptions)
                        }
                    }
                }

                override fun onFailure(e: Throwable) {
                    // Обработка ошибки
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(callback)

        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.setLocationPermission(true)
        } else {
            requestLocationPermission()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            viewModel.setLocationPermission(
                grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
            )
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        _binding?.mapView?.onDestroy()
        super.onDestroy()
    }
}
