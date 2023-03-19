package youngdevs.production.youngmoscow.di

import android.content.Context
import dagger.Module
import dagger.Provides
import youngdevs.production.youngmoscow.data.repository.KudaGoRepository
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import youngdevs.production.youngmoscow.presentation.main.MainViewModelFactory
import youngdevs.production.youngmoscow.presentation.settings.SettingsViewModelFactory

@Module
class ViewModelModule(private val context: Context) {
    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideMainViewModelFactory(repository: KudaGoRepository): MainViewModelFactory {
        return MainViewModelFactory(repository)
    }

    @Provides
    fun provideSettingsViewModel(authenticateUserUseCase: AuthenticateUserUseCase): SettingsViewModelFactory {
        return SettingsViewModelFactory(authenticateUserUseCase)
    }
}