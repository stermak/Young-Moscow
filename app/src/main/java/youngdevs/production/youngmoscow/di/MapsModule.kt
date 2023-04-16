package youngdevs.production.youngmoscow.di

import android.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent
import youngdevs.production.youngmoscow.presentation.ui.fragments.MapsFragment


@Module
@InstallIn(SingletonComponent::class) // Здесь указывается, в каком компоненте будет доступна зависимость (в данном случае FragmentComponent)

abstract class MapsModule {
    @Binds
    abstract fun bindMapProvider(provider: MapsProviderImpl?): MapProvider?

    companion object {
        @Provides
        fun provideGoogleMap(fragment: MapsFragment): GoogleMap {
            return (fragment.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?).getMap()
        }
    }
}
