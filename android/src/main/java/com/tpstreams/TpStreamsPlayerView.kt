package com.tpstreams

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ThemedReactContext

class TpStreamsPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var videoId: String? = null
    private var accessToken: String? = null
    private var fragmentModule: FragmentModule? = null

    init {
        if (context is ThemedReactContext) {
            val reactContext = context.reactApplicationContext
            val activity = context.currentActivity as? FragmentActivity

            if (activity != null) {
                fragmentModule = FragmentModule(reactContext)
            }
        }
    }

    fun setVideoId(id: String) {
        videoId = id
        updateFragment()
    }

    fun setAccessToken(token: String) {
        accessToken = token
        updateFragment()
    }

    private fun updateFragment() {
        if (!videoId.isNullOrEmpty() && !accessToken.isNullOrEmpty()) {
            fragmentModule?.showCustomFragment(videoId!!, accessToken!!)
        }
    }
}
