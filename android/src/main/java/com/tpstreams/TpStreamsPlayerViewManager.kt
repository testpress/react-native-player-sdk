package com.tpstreams

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext

class TpStreamsPlayerViewManager : SimpleViewManager<TpStreamsPlayerView>() {

    override fun getName() = "TpStreamsPlayerView"

    override fun createViewInstance(reactContext : ThemedReactContext) :TpStreamsPlayerView {
        return TpStreamsPlayerView(reactContext)
    }
}