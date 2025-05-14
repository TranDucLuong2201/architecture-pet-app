package com.dluong.pet

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetsApplication : Application()
    // This class is empty because we are using Hilt for dependency injection.
    // Hilt will automatically generate the necessary code to set up the application.
    // You can add any application-level logic here if needed.
