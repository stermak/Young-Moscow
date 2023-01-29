package youngdevs.production.youngmoscow.di

import android.content.Context
import com.example.YoungMoscow.presentation.settings.SettingsViewModelFactory
import dagger.Module
import dagger.Provides
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase

@Module
class ViewModelModule(private val context: Context) {
    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideSettingsViewModel(authenticateUserUseCase: AuthenticateUserUseCase): SettingsViewModelFactory {
        return SettingsViewModelFactory(authenticateUserUseCase)
    }


}