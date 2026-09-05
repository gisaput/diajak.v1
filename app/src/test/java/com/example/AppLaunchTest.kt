package com.example

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class AppLaunchTest {
    @Test
    fun testAppLaunchesWithoutCrashing() {
        val controller = Robolectric.buildActivity(MainActivity::class.java).setup()
        ShadowLooper.idleMainLooper(3000)
        controller.get()
    }
}
