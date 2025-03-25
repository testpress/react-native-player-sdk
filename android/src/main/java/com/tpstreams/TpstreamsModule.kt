package com.tpstreams

import android.os.Handler
import android.os.Looper
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.tpstream.player.TPStreamsSDK
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.Arguments
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.bridge.ReactContext

@ReactModule(name = TpstreamsModule.NAME)
class TpstreamsModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) { 
  private val listeners = mutableSetOf<String>()

  init {
    companionReactContext = reactContext
  }

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun addListener(eventName: String?) {
    eventName?.let {
      listeners.add(it)
    }
  }

  @ReactMethod
  fun removeListeners(count: Int) {
    for (i in 0 until count) {
      if (listeners.isNotEmpty()) {
        listeners.remove(listeners.first())
      }
    }
  }

  @ReactMethod
  fun initializeTPSPlayer(orgId: String) {
    TPStreamsSDK.initialize(TPStreamsSDK.Provider.TPStreams, orgId)
  }

  @ReactMethod
  fun release() {
      runOnMainThread {
          getPlayerFragment()?.release()
      }
  }

  @ReactMethod
    fun play() {
        runOnMainThread {
            getPlayerFragment()?.play()
        }
    }

  @ReactMethod
  fun pause() {
      runOnMainThread {
          getPlayerFragment()?.pause()
      }
  }

  @ReactMethod
  fun seekTo(position: Double) {
      runOnMainThread {
          getPlayerFragment()?.seekTo(position.toLong())
      }
  }

  @ReactMethod
  fun getCurrentTime(promise: Promise) {
      runOnMainThread {
          getPlayerFragment()?.let {
              promise.resolve(it.getCurrentTime().toDouble())
          } ?: promise.reject("PLAYER_NOT_READY", "Player is not initialized")
      }
  }
  
  @ReactMethod
  fun getDuration(promise: Promise) {
      runOnMainThread {
          getPlayerFragment()?.let {
              promise.resolve(it.getDuration().toDouble())
          } ?: promise.reject("PLAYER_NOT_READY", "Player is not initialized")
      }
  }
  
  @ReactMethod
  fun getBufferedTime(promise: Promise) {
      runOnMainThread {
          getPlayerFragment()?.let {
              promise.resolve(it.getBufferedTime().toDouble())
          } ?: promise.reject("PLAYER_NOT_READY", "Player is not initialized")
      }
  }

  @ReactMethod
  fun getPlaybackState(promise: Promise) {
      runOnMainThread {
          getPlayerFragment()?.let {
              promise.resolve(it.getPlaybackState())
          } ?: promise.reject("PLAYER_NOT_READY", "Player is not initialized")
      }
  }

  @ReactMethod
  fun getPlayWhenReady(promise: Promise) {
      runOnMainThread {
          getPlayerFragment()?.let {
              promise.resolve(it.getPlayWhenReady())
          } ?: promise.reject("PLAYER_NOT_READY", "Player is not initialized")
      }
  }

  @ReactMethod
  fun setPlayWhenReady(playWhenReady: Boolean) {
      runOnMainThread {
          getPlayerFragment()?.setPlayWhenReady(playWhenReady)
      }
  }

  @ReactMethod
  fun getPlaybackSpeed(promise: Promise) {
      runOnMainThread {
          getPlayerFragment()?.let {
              promise.resolve(it.getPlaybackSpeed().toDouble())
          } ?: promise.reject("PLAYER_NOT_READY", "Player is not initialized")
      }
  }

  @ReactMethod
  fun setPlaybackSpeed(speed: Float) {
      runOnMainThread {
          getPlayerFragment()?.setPlaybackSpeed(speed.toFloat())
      }
  }

  private fun getPlayerFragment(): PlayerFragment? {
      return PlayerFragment.instance?.takeIf { it.player != null }
  }

  private fun runOnMainThread(action: () -> Unit) {
        companionReactContext?.runOnUiQueueThread(action)
  }

  companion object {
    const val NAME = "Tpstreams"
    var companionReactContext: ReactApplicationContext? = null
    fun sendEvent(eventName: String, params: Any?) {
        companionReactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
        ?.emit(eventName, params)
    }
  }
}
