package com.tpstreams

import kotlinx.coroutines.*
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ThemedReactContext

class TpStreamsPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), CoroutineScope {

    private var videoId: String? = null
    private var accessToken: String? = null
    private var enableDownload: Boolean = true
    private var autoPlay :Boolean = true
    private var startAt :Int = 0
    private var offlineLicenseExpireTime :Int = 15
    private var fragmentModule: FragmentModule? = null

    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + job
    private var updateJob: Job? = null

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

    fun setEnableDownload(enableDownload: Boolean?) {
        this.enableDownload = enableDownload ?: true
        updateFragment()
    }

    fun setAutoPlay(autoPlay: Boolean?) {
        this.autoPlay = autoPlay ?: true
        updateFragment()
    }

    fun setStartTime(startAt: Int?) {
        this.startAt = startAt ?: 0
        updateFragment()
    }

    fun setOfflineLicenseExpireTime(offlineLicenseExpireTime: Int?) {
        this.offlineLicenseExpireTime = offlineLicenseExpireTime ?: 15
        updateFragment()
    }

    private fun updateFragment() {
        if (!videoId.isNullOrEmpty() && !accessToken.isNullOrEmpty()) {
            updateJob?.cancel()
            updateJob = launch {
                delay(50)
                fragmentModule?.closeCustomFragment()
                fragmentModule?.showCustomFragment(
                    videoId!!,
                    accessToken!!,
                    enableDownload,
                    autoPlay ?: true,
                    startAt,
                    offlineLicenseExpireTime,
                )
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        coroutineContext.cancel()
        fragmentModule?.closeCustomFragment()
    }
    
}
