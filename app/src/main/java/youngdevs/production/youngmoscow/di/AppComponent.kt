package youngdevs.production.youngmoscow.di

import dagger.Component
import youngdevs.production.youngmoscow.presentation.authentication.LoginFragment
import youngdevs.production.youngmoscow.presentation.authentication.RegistrationFragment
import youngdevs.production.youngmoscow.presentation.event.EventDetailsFragment
import youngdevs.production.youngmoscow.presentation.main.MainFragment
import youngdevs.production.youngmoscow.presentation.maps.MapsFragment
import youngdevs.production.youngmoscow.presentation.settings.SettingsFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DatabaseModule::class, ViewModelModule::class,
        UseCaseModule::class, RepositoryModule::class, KudaGoModule::class]
)
interface AppComponent {
    fun inject(loginFragment: LoginFragment)
    fun inject(registrationFragment: RegistrationFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(mapsFragment: MapsFragment)
    fun inject(mainFragment: MainFragment)
    fun inject(eventDetailsFragment: EventDetailsFragment)
}