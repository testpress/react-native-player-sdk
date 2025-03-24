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

class PlayerFragment : Fragment() {

  lateinit var player: TpStreamPlayer
  lateinit var playerView: TPStreamPlayerView
  lateinit var playerFragment: TpStreamPlayerFragment
  private var videoId :String = ""
  private var accessToken :String = ""
  private var enableDownloadSupport :Boolean = true
  private var setAutoPlay :Boolean = true

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
      .build()
    requireActivity().runOnUiThread {
      player.load(parameters)
    }
  }

  private fun addPlayerListener(){
    player.setListener( object : TPStreamPlayerListener {
      override fun onPlaybackStateChanged(playbackState: Int) {
        Log.d("TAG", "onPlaybackStateChanged: $playbackState")
      }

      override fun onAccessTokenExpired(videoId: String, callback: (String) -> Unit) {
        callback(accessToken)
      }

      override fun onMarkerCallback(timesInSeconds: Long) {
        requireActivity().runOnUiThread {
          Toast.makeText(requireContext(),"Time $timesInSeconds", Toast.LENGTH_SHORT).show()
        }
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
