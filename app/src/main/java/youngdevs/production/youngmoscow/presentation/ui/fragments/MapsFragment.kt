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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.FragmentMapsBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.MapsViewModel

// Фрагмент, отображающий карту и текущее местоположение пользователя
@AndroidEntryPoint // аннотация для использования Hilt DI
class MapsFragment : Fragment() {

    // Поле для привязки View Binding
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!


    // View-компонент для отображения карты
    private lateinit var mapView: MapView

    // ViewModel для работы с данными
    private val viewModel: MapsViewModel by viewModels()

    // Код запроса на получение разрешения на доступ к местоположению
    private val locationRequestCode = 1000
    private val START_ZOOM = 14.0f
    private var isMapZoomedToCurrentLocation = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Инициализация View Binding и возвращение корневого View макета фрагмента
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация MapView
        mapView = binding.mapView

        val fab: FloatingActionButton = binding.fab
        fab.setOnClickListener {
            moveToCurrentLocation()
        }

        if (viewModel.hasLocationPermission()) {
            // Перемещение к текущему местоположению, если разрешение на доступ к местоположению уже получено
            moveToCurrentLocation()
        } else {
            // Запрос разрешения на доступ к местоположению
            viewModel.requestLocationPermission(requireActivity())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Перемещение к текущему местоположению после получения разрешения на доступ к местоположению
                moveToCurrentLocation()
            }
        }
    }

    private fun addPlacemarkToMap(latitude: Double, longitude: Double) {
        // Создаем метку
        val placemarkMapObject = mapView.map.mapObjects.addPlacemark(Point(latitude, longitude))

        // Задаем свойства метки
        placemarkMapObject.setIcon(ImageProvider.fromResource(requireContext(),
            android.R.drawable.btn_star_big_on
        ))
        placemarkMapObject.isVisible = true
        placemarkMapObject.opacity = 0.8f
    }

    private fun moveToCurrentLocation() {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (viewModel.hasLocationPermission()) {
            try {
                val locationProvider = LocationManager.GPS_PROVIDER
                val lastKnownLocation = locationManager.getLastKnownLocation(locationProvider)
                if (lastKnownLocation != null) {
                    // Получаем текущую позицию камеры
                    val cameraPosition = mapView.map.cameraPosition

                    // Перемещение карты к текущему местоположению пользователя с сохранением текущего масштаба и положения камеры
                    if (!isMapZoomedToCurrentLocation) {
                        // Если карта еще не была приближена к текущей геолокации, то использовать начальный масштаб
                        mapView.map.move(
                            CameraPosition(
                                Point(lastKnownLocation.latitude, lastKnownLocation.longitude),
                                START_ZOOM, cameraPosition.azimuth, cameraPosition.tilt
                            )
                        )

                        isMapZoomedToCurrentLocation = true // Установить флаг, что карта была приближена к текущей геолокации
                    } else {
                        // Если карта уже была приближена к текущей геолокации, то использовать текущий масштаб
                        mapView.map.move(
                            CameraPosition(
                                Point(lastKnownLocation.latitude, lastKnownLocation.longitude),
                                cameraPosition.zoom, cameraPosition.azimuth, cameraPosition.tilt
                            )
                        )
                    }

                    // Добавляем метку на текущее местоположение пользователя
                    addPlacemarkToMap(lastKnownLocation.latitude, lastKnownLocation.longitude)
                }
            } catch (e: SecurityException) {
                // Обработка исключения SecurityException при доступе к местоположению
            }
        }
    }


    override fun onStart() {
        super.onStart()
        // Вызов метода onStart у MapView и MapKitFactory для инициализации карты
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        // Вызов метода onStop у MapView и MapKitFactory для завершения работы с картой
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
