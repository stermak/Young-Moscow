package youngdevs.production.youngmoscow.app

import android.app.Application
import youngdevs.production.youngmoscow.di.AppComponent
import youngdevs.production.youngmoscow.di.DaggerAppComponent
import youngdevs.production.youngmoscow.di.ViewModelModule

class YoungMoscow : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .viewModelModule(ViewModelModule(this))
            .build()
    }
}