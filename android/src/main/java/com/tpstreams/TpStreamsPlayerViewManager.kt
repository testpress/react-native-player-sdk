package com.tpstreams

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class TpStreamsPlayerViewManager : SimpleViewManager<TpStreamsPlayerView>() {

    override fun getName() = "TpStreamsPlayerView"

    override fun createViewInstance(reactContext : ThemedReactContext) :TpStreamsPlayerView {
        return TpStreamsPlayerView(reactContext)
    }

    @ReactProp(name = "videoId")
    fun setVideoId(view: TpStreamsPlayerView, videoId: String?) {
        videoId?.let { view.setVideoId(it) }
    }

    @ReactProp(name = "accessToken")
    fun setAccessToken(view: TpStreamsPlayerView, accessToken: String?) {
        accessToken?.let { view.setAccessToken(it) }
    }
    
    @ReactProp(name = "enableDownload")
    fun setEnableDownload(view: TpStreamsPlayerView, enableDownload: Boolean?) {
        view.setEnableDownload(enableDownload)
    }

    @ReactProp(name = "autoPlay")
    fun setAutoPlay(view: TpStreamsPlayerView, autoPlay: Boolean?) {
        view.setAutoPlay(autoPlay)
    }

    @ReactProp(name = "startAt")
    fun setStartTime(view: TpStreamsPlayerView, startAt: Int?) {
        view.setStartTime(startAt ?: 0)
    }

    @ReactProp(name = "offlineLicenseExpireTime")
    fun setOfflineLicenseExpireTime(view: TpStreamsPlayerView, offlineLicenseExpireTime: Int?) {
        view.setOfflineLicenseExpireTime(offlineLicenseExpireTime ?: 15)
    }
}





