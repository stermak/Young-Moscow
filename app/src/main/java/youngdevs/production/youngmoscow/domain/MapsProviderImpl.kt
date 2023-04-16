package youngdevs.production.youngmoscow.domain

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import youngdevs.production.youngmoscow.di.MapsModule

class MapsProviderImpl(private val mapView: MapView) : MapsModule {

    private var googleMap: GoogleMap? = null

    override fun getMap(onMapReadyCallback: OnMapReadyCallback) {
        mapView.getMapAsync { map ->
            googleMap = map
            onMapReadyCallback.onMapReady(map)
        }
    }

    override fun onResume() {
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
    }

    override fun getGoogleMap(): GoogleMap? {
        return googleMap
    }
}