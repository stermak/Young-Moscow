package youngdevs.production.youngmoscow.di

import dagger.Component
import youngdevs.production.youngmoscow.presentation.authentication.LoginFragment
import youngdevs.production.youngmoscow.presentation.authentication.RegistrationFragment
import youngdevs.production.youngmoscow.presentation.settings.SettingsFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DatabaseModule::class, ViewModelModule::class,
        UseCaseModule::class, RepositoryModule::class]
)
interface AppComponent {
    fun inject(loginFragment: LoginFragment)
    fun inject(registrationFragment: RegistrationFragment)
    fun inject(settingsFragment: SettingsFragment)
}