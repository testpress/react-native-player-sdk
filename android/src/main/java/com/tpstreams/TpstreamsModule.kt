package com.tpstreams

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.tpstream.player.TPStreamsSDK

@ReactModule(name = TpstreamsModule.NAME)
class TpstreamsModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) { 

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun initializeTPSPlayer(orgId: String) {
    TPStreamsSDK.initialize(TPStreamsSDK.Provider.TPStreams, orgId)
  }

  companion object {
    const val NAME = "Tpstreams"
  }
}
