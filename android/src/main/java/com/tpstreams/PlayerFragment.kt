package com.tpstreams

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tpstream.player.TPStreamPlayerListener
import com.tpstream.player.TpInitParams
import com.tpstream.player.TpStreamPlayer
import com.tpstream.player.ui.InitializationListener
import com.tpstream.player.ui.TPStreamPlayerView
import com.tpstream.player.ui.TpStreamPlayerFragment
import com.tpstream.player.constants.PlaybackError
import com.tpstream.player.Tracks
import com.tpstream.player.Timeline
import com.tpstream.player.DeviceInfo
import com.tpstreams.TpstreamsModule

internal typealias onAccessTokenCallbase = (String) -> Unit

class PlayerFragment : Fragment() {

  lateinit var player: TpStreamPlayer
  lateinit var playerView: TPStreamPlayerView
  lateinit var playerFragment: TpStreamPlayerFragment
  private var videoId :String = ""
  private var accessToken :String = ""
  private var enableDownloadSupport :Boolean = true
  private var setAutoPlay :Boolean = true
  private var startAt :Int = 0
  private var offlineLicenseExpireTime :Int = 60 * 60 * 24 * 15 //15 days

  private var accessTokenCallback : onAccessTokenCallbase? = null

  companion object {
    var instance: PlayerFragment? = null
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val bundle = arguments
    if (bundle != null) {
      videoId = bundle.getString("VIDEO_ID") ?: ""
      accessToken = bundle.getString("ACCESS_TOKEN") ?: ""
      enableDownloadSupport = bundle.getBoolean("ENABLE_DOWNLOAD_SUPPORT", true)
      setAutoPlay = bundle.getBoolean("AUTO_PLAY", true)
      startAt = bundle.getInt("START_AT", 0)
      offlineLicenseExpireTime = bundle.getInt("OFFLINE_LICENSE_EXPIRE_TIME", offlineLicenseExpireTime)

    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_player, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    instance = this
    playerFragment = childFragmentManager.findFragmentById(R.id.tpstream_player_fragment) as TpStreamPlayerFragment
    playerFragment.setOnInitializationListener(object: InitializationListener {

      override fun onInitializationSuccess(player: TpStreamPlayer) {
        this@PlayerFragment.player = player
        playerView = playerFragment.tpStreamPlayerView
        loadPLayer()
        addPlayerListener()
      }
    })
  }

  fun loadPLayer() {
    val parameters = TpInitParams.Builder()
      .setVideoId(videoId)
      .setAccessToken(accessToken)
      .enableDownloadSupport(enableDownloadSupport)
      .setAutoPlay(setAutoPlay)
      .startAt(startAt.toLong())
      .setOfflineLicenseExpireTime(offlineLicenseExpireTime)
      .build()
    requireActivity().runOnUiThread {
      player.load(parameters)
    }
  }

  private fun sendEvent(eventName: String, params: Any?) {
    TpstreamsModule.sendEvent(eventName, params)
  }

  private fun addPlayerListener(){
    player.setListener( object : TPStreamPlayerListener {
      override fun onPlaybackStateChanged(playbackState: Int) {
        sendEvent("onPlaybackStateChanged", playbackState)
      }

      override fun onAccessTokenExpired(videoId: String, callback: (String) -> Unit) {
        Log.d("ReactNativeJS", "onTokenExpired: $videoId")
        accessTokenCallback = callback
        sendEvent("onAccessTokenExpired", videoId)
      }

      override fun onMarkerCallback(timesInSeconds: Long) {
        requireActivity().runOnUiThread {
          Toast.makeText(requireContext(),"Time $timesInSeconds", Toast.LENGTH_SHORT).show()
        }
        sendEvent("onMarkerCallback", timesInSeconds)
      }

      override fun onDeviceInfoChanged(deviceInfo: DeviceInfo) {
        sendEvent("onDeviceInfoChanged", deviceInfo.toString())
      }

      override fun onFullScreenChanged(isFullScreen: Boolean) {
        sendEvent("onFullScreenChanged", isFullScreen)
      }

      override fun onIsLoadingChanged(isLoading: Boolean) {
        sendEvent("onIsLoadingChanged", isLoading)
      }

      override fun onIsPlayingChanged(isPlaying: Boolean) {
        sendEvent("onIsPlayingChanged", isPlaying)
      }

      override fun onPlayerError(playbackError: PlaybackError) {
        sendEvent("onPlayerError", playbackError.toString())
      }

      override fun onSeekBackIncrementChanged(incrementMs: Long) {
        sendEvent("onSeekBackIncrementChanged", incrementMs)
      }

      override fun onSeekForwardIncrementChanged(incrementMs: Long) {
        sendEvent("onSeekForwardIncrementChanged", incrementMs)
      }

      override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        sendEvent("onTimelineChanged", null)
      }

      override fun onTracksChanged(tracks: Tracks) {
        sendEvent("onTracksChanged", tracks.toString())
      }
    })
  }
  // Core Player Controls
  fun play() {
    requireActivity().runOnUiThread {
        player.play()
    }
  }

  fun pause() {
    requireActivity().runOnUiThread {
        player.pause()
    }
  }

  fun seekTo(position: Long) {
    requireActivity().runOnUiThread {
        player.seekTo(position)
    }
  }

  fun release() {
    requireActivity().runOnUiThread {
        player.release()
    }
  }

  fun load(params: TpInitParams) {
    requireActivity().runOnUiThread {
        player.load(params)
    }
  }

  // Playback & State Management
  fun getCurrentTime(): Double = player.getCurrentTime().toDouble()

  fun getDuration(): Double = player.getDuration().toDouble()

  fun getBufferedTime(): Double = player.getBufferedTime().toDouble()

  fun getPlaybackState(): Int = player.getPlaybackState()

  fun getPlayWhenReady(): Boolean = player.getPlayWhenReady()

  fun setPlayWhenReady(playWhenReady: Boolean) {
    requireActivity().runOnUiThread {
        player.setPlayWhenReady(playWhenReady)
    }
  }


  // Speed & Volume
  fun getPlaybackSpeed(): Float = player.getPlayBackSpeed()

  fun setPlaybackSpeed(speed: Float) {
    requireActivity().runOnUiThread {
        player.setPlaybackSpeed(speed)
    }
  }

  fun setNewAccessToken(token: String) {
    accessTokenCallback?.invoke(token)
    accessTokenCallback = null
  }

  override fun onResume() {
    super.onResume()
    Log.d("ReactNativeJS", "onResume: ")
  }

  override fun onDestroy() {
    super.onDestroy()
    instance = null
    Log.d("ReactNativeJS", "onDestroy: ")
  }

}
