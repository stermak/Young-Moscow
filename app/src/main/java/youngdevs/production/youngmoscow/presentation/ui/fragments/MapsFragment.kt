package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.GeoApiContext
import com.google.maps.NearbySearchRequest
import com.google.maps.model.PlacesSearchResponse
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.FragmentMapsBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.MapsViewModel
import java.io.IOException

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
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private lateinit var geoApiContext: GeoApiContext

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        Places.initialize(requireContext(), getString(R.string.google_maps_key))
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        placesClient = Places.createClient(requireContext())
        geoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_key))
            .build()
        return binding.root
    }

    @SuppressLint("PotentialBehaviorOverride", "MissingPermission")

    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap

        viewModel.locationPermission.observe(viewLifecycleOwner) { isGranted ->
            if (isGranted) {
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
                getLastKnownLocation()
            }
        }

        googleMap.setOnMarkerClickListener { marker ->
            val markerLocation = marker.position
            showRouteToPlace(markerLocation)
            true
        }

        showLandmarks()
    }


    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
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


    @SuppressLint("MissingPermission")
    private fun showLandmarks() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                val placeTypes = listOf(
                    com.google.maps.model.PlaceType.PARK,
                    com.google.maps.model.PlaceType.MUSEUM,
                    com.google.maps.model.PlaceType.ART_GALLERY
                )

                for (type in placeTypes) {
                    val request = NearbySearchRequest(geoApiContext)
                    request.location(com.google.maps.model.LatLng(currentLatLng.latitude, currentLatLng.longitude))
                    request.radius(1000) // Радиус в метрах, установите подходящее значение
                    request.type(type)

                    lifecycleScope.launch {
                        try {
                            val placesResponse = withContext(Dispatchers.IO) { request.await() }

                            for (result in placesResponse.results) {
                                val latLng = LatLng(result.geometry.location.lat, result.geometry.location.lng)
                                googleMap.addMarker(MarkerOptions().position(latLng).title(result.name))
                            }
                        } catch (e: Exception) {
                            Log.e("MapsFragment", "Exception: %s", e)
                        }
                    }
                }
            }
        }
    }


    private var currentPolyline: Polyline? = null

    @SuppressLint("MissingPermission")
    private fun showRouteToPlace(destination: LatLng) {
        val path = ArrayList<LatLng>()
        currentPolyline?.remove()
        activity?.runOnUiThread {
            val lineOptions = PolylineOptions().addAll(path).width(10f).color(Color.BLUE)
            currentPolyline = googleMap.addPolyline(lineOptions)
        }

        // Получить текущее местоположение и показать маршрут от текущего местоположения до маркера
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val origin = LatLng(it.latitude, it.longitude)
                val directionsRequest = "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${destination.latitude},${destination.longitude}&key=${getString(R.string.google_maps_key)}"
                val directionsClient = OkHttpClient()
                val request = Request.Builder().url(directionsRequest).build()

                directionsClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("MapsFragment", "Error getting directions", e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.body?.let { responseBody ->
                            val jsonResponse = JSONObject(responseBody.string())
                            val routes = jsonResponse.getJSONArray("routes")
                            if (routes.length() > 0) {
                                val route = routes.getJSONObject(0)
                                val legs = route.getJSONArray("legs")
                                val steps = legs.getJSONObject(0).getJSONArray("steps")

                                for (i in 0 until steps.length()) {
                                    val step = steps.getJSONObject(i)
                                    val points = step.getJSONObject("polyline").getString("points")
                                    path.addAll(viewModel.decodePoly(points))
                                }

                                activity?.runOnUiThread {
                                    val lineOptions = PolylineOptions().addAll(path).width(10f).color(
                                        Color.BLUE)
                                    googleMap.addPolyline(lineOptions)
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
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
