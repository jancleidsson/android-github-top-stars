package com.jss.githubtopstars.utils

import android.os.Bundle
import android.view.WindowManager
import androidx.test.runner.AndroidJUnitRunner
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage

//Runner to prevent screen of during instrumented tests
class CustomJUnitRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle) {
        super.onCreate(arguments)
        ActivityLifecycleMonitorRegistry.getInstance().addLifecycleCallback { activity, stage ->
            if (stage === Stage.PRE_ON_CREATE) {
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}