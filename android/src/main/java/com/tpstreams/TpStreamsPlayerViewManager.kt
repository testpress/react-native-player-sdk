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
}





