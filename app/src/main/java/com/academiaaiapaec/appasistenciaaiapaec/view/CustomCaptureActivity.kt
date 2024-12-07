package com.academiaaiapaec.appasistenciaaiapaec.view

import android.content.pm.ActivityInfo
import com.journeyapps.barcodescanner.CaptureActivity

class CustomCaptureActivity: CaptureActivity() {
    override fun onResume() {
        super.onResume()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Forzar orientación vertical
    }
}