package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil
import com.google.maps.model.DirectionsResult
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.FragmentMapsBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.MapsViewModel

// Фрагмент, отображающий карту и текущее местоположение пользователя
@AndroidEntryPoint
class MapsFragment : Fragment() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapsViewModel by viewModels()
    private lateinit var googleMap: GoogleMap

    @SuppressLint("PotentialBehaviorOverride")
    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        viewModel.locationPermission.observe(viewLifecycleOwner) { isGranted ->
            if (isGranted) {
                enableUserLocation()
            }
        }
        googleMap.setOnPoiClickListener { poi ->
            val poiMarker = googleMap.addMarker(MarkerOptions().position(poi.latLng).title(poi.name))
            poiMarker?.showInfoWindow()

            val destination = poi.latLng
            viewModel.getDirections(
                currentLocation = viewModel.currentLocation.value?.toLatLng() ?: LatLng(0.0, 0.0),
                destination = destination,
                apiKey = getString(R.string.google_maps_key)
            ).observe(viewLifecycleOwner) { directions ->
                drawRoute(directions)
            }

        }

        googleMap.setOnMarkerClickListener { marker ->
            val destination = marker.position
            viewModel.getDirections(
                currentLocation = (viewModel.currentLocation.value ?: LatLng(0.0, 0.0)) as LatLng,
                destination = destination,
                apiKey = getString(R.string.google_maps_key)
            ).observe(viewLifecycleOwner) { directions ->
                drawRoute(directions)
            }
            true
        }
        findPlaces(googleMap, Place.Type.MUSEUM, 1000)
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
        val moscowLatLng = LatLng(55.7522200, 37.6155600)
        val zoomLevel = 10f
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moscowLatLng, zoomLevel))
    }

    private fun findPlaces(googleMap: GoogleMap, type: Place.Type, radius: Int) {
        val placesClient = Places.createClient(requireContext())
        val currentLatLng = viewModel.currentLocation.value?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(0.0, 0.0)
        val point1 = SphericalUtil.computeOffset(currentLatLng, radius.toDouble(), 0.0)
        val point2 = SphericalUtil.computeOffset(currentLatLng, radius.toDouble(), 180.0)
        val southWest = if (point1.latitude < point2.latitude) point1 else point2
        val northEast = if (point1.latitude < point2.latitude) point2 else point1
        val locationBias = RectangularBounds.newInstance(southWest, northEast)

        val request = FindAutocompletePredictionsRequest.builder()
            .setLocationBias(locationBias)
            .setCountries("RU") // Optional: limit search to a specific country
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setQuery(type.toString())
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            for (prediction in response.autocompletePredictions) {
                val placeId = prediction.placeId
                val placeFields = listOf(Place.Field.NAME, Place.Field.LAT_LNG)
                val fetchPlaceRequest = FetchPlaceRequest.newInstance(placeId, placeFields)

                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener { fetchPlaceResponse ->
                    val place = fetchPlaceResponse.place
                    if (place.types?.contains(type) == true) {
                        googleMap.addMarker(MarkerOptions().position(place.latLng ?: LatLng(0.0, 0.0)).title(place.name))
                    }
                }.addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        Log.e("MapsFragment", "Place not found: ${exception.statusCode}")
                    }
                }
            }
        }
    }

    private fun drawRoute(directions: DirectionsResult) {
        val path = PolyUtil.decode(directions.routes[0].overviewPolyline.encodedPath)
        val polylineOptions = PolylineOptions().addAll(path).color(Color.BLUE).width(10f)
        googleMap.addPolyline(polylineOptions)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        Places.initialize(requireContext(), getString(R.string.google_maps_key))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(callback)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.setLocationPermission(true)
        } else {
            requestLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            viewModel.setLocationPermission(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

    fun Location.toLatLng(): LatLng {
        return LatLng(this.latitude, this.longitude)
    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            viewModel.getLastKnownLocation(requireContext()).addOnSuccessListener { location ->
                location?.let {
                    viewModel.updateCurrentLocation(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        _binding?.mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}
