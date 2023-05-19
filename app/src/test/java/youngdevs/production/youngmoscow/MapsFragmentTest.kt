package youngdevs.production.youngmoscow

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.GeoApiContext
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import youngdevs.production.youngmoscow.presentation.ui.fragments.MapsFragment

@RunWith(MockitoJUnitRunner::class)
class MapsFragmentTest {

    @Mock
    private lateinit var mapView: MapView

    @Mock
    private lateinit var googleMap: GoogleMap

    @Mock
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Mock
    private lateinit var placesClient: PlacesClient

    @Mock
    private lateinit var geoApiContext: GeoApiContext

    @Mock
    private lateinit var context: Context

    private lateinit var fragment: MapsFragment


}
