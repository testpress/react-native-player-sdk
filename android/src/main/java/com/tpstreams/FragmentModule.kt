package com.tpstreams

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactApplicationContext
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.tpstream.player.data.Asset
import com.tpstream.player.offline.TpStreamDownloadManager
import com.tpstream.player.TPStreamsSDK
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap


class FragmentModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  lateinit var viewModel: DownloadListViewModel
  private var assets = listOf<Asset>()

  override fun initialize() {
    super.initialize()
    (currentActivity as? FragmentActivity)?.let {
      viewModel = DownloadListViewModel.init(it)
    }
    Log.d("FragmentModule", "initialize: ")
  }

  override fun getName(): String {
    return "FragmentModule"
  }

  @ReactMethod
  fun showCustomFragment(
    videoId: String,
    accessToken: String,
    enableDownload: Boolean,
    autoPlay: Boolean,
    startAt: Int,
    offlineLicenseExpireTime: Int,
    downloadMetadata: ReadableMap?,
  ) {
    Log.e("FragmentModule", "showCustomFragment() called")
    // Ensure the currentActivity is a FragmentActivity
    val activity = currentActivity as? FragmentActivity
    activity?.let {
      // Begin the fragment transaction
      val fragmentTransaction = it.supportFragmentManager.beginTransaction()

      // Create a new fragment instance
      val bundle = Bundle()
      bundle.putString("VIDEO_ID", videoId)
      bundle.putString("ACCESS_TOKEN", accessToken)
      bundle.putBoolean("ENABLE_DOWNLOAD_SUPPORT", enableDownload)
      bundle.putBoolean("AUTO_PLAY", autoPlay)
      bundle.putInt("START_AT", startAt)
      bundle.putInt("OFFLINE_LICENSE_EXPIRE_TIME", offlineLicenseExpireTime)
      bundle.putSerializable("DOWNLOAD_METADATA", downloadMetadata?.toHashMapString())
      val fragment = PlayerFragment()
      fragment.setArguments(bundle)

      // Find a container to add the fragment
      val fragmentContainerId = android.R.id.content // or you could specify your own container ID

      // Add the fragment
      fragmentTransaction.replace(fragmentContainerId, fragment)
      fragmentTransaction.addToBackStack(null)  // Optional: Add fragment to back stack
      fragmentTransaction.commit()
    } ?: run {
      // Handle the error if the activity is not a FragmentActivity
      Log.e("ReactNativeJS", "Current activity is not a FragmentActivity")
    }
  }

  @ReactMethod
  fun closeCustomFragment() {
      Log.e("FragmentModule", "closeCustomFragment() called")
  
      // Ensure currentActivity is a FragmentActivity
      val activity = currentActivity as? FragmentActivity
      if (activity == null || activity.isFinishing || activity.isDestroyed) {
          Log.e("FragmentModule", "Activity is null, finishing, or destroyed")
          return
      }
  
      val fragmentManager = activity.supportFragmentManager
      val playerFragment = fragmentManager.findFragmentByTag("PLAYER_FRAGMENT") as? PlayerFragment
  
      if (playerFragment != null) {
          Log.d("FragmentModule", "Removing PlayerFragment")
          fragmentManager.beginTransaction()
              .remove(playerFragment)
              .commitAllowingStateLoss() // Avoid IllegalStateException
      } else if (fragmentManager.backStackEntryCount > 0) {
          Log.d("FragmentModule", "Popping back stack")
          fragmentManager.popBackStack()
      } else {
          Log.e("FragmentModule", "No fragments to remove")
      }
  }

  @ReactMethod
  fun observeDownloadData() {
    // Ensure this code runs on the main thread
    currentActivity?.runOnUiThread {
      val activity = currentActivity as? FragmentActivity
      activity?.let {
        // Observe LiveData only on the main thread
        viewModel.getDownloadData().observe(it) { assets ->
          this.assets = assets ?: emptyList()
          // Emit event to React Native
          val eventMap = Arguments.createMap()
          val assetsList = Arguments.createArray()

          assets?.forEachIndexed { index , asset ->
            val assetMap = Arguments.createMap()
            assetMap.putString("id", index.toString())
            assetMap.putString("videoId", asset.id)
            assetMap.putString("title", asset.title)
            assetMap.putString("percentage", asset.video.percentageDownloaded.toString())
            assetMap.putString("status", asset.video.downloadState?.name ?: "Unknown")
            assetMap.putString("duration", asset.video.duration.toString())
            assetMap.putMap("metadata", asset.metadata?.toWritableMap() ?: null)
            assetsList.pushMap(assetMap)
          }

          eventMap.putArray("assets", assetsList)
          reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("onDownloadDataChanged", eventMap)
        }
      } ?: run {
        Log.e("FragmentModule", "Current activity is not a FragmentActivity")
      }
    }
  }

  fun ReadableMap.toHashMapString(): HashMap<String, String> {
    val result = HashMap<String, String>()
    val iterator = this.keySetIterator()
    while (iterator.hasNextKey()) {
        val key = iterator.nextKey()
        val value = this.getString(key) // Ensuring values are strings
        if (value != null) {
            result[key] = value
        }
    }
    return result
  }

  fun Map<String, String>.toWritableMap(): ReadableMap {
    val writableMap: WritableMap = WritableNativeMap()
    for ((key, value) in this) {
        writableMap.putString(key, value)
    }
    return writableMap
  }

  @ReactMethod
  fun pauseDownload(videoId: String) {
    val asset = assets.first { it.id == videoId }
    viewModel.pauseDownload(asset)
  }

  @ReactMethod
  fun resumeDownload(videoId: String) {
    val asset = assets.first { it.id == videoId }
    viewModel.resumeDownload(asset)
  }

  @ReactMethod
  fun cancelDownload(videoId: String) {
    val asset = assets.first { it.id == videoId }
    viewModel.cancelDownload(asset)
  }

  @ReactMethod
  fun deleteDownload(videoId: String) {
    val asset = assets.first { it.id == videoId }
    viewModel.deleteDownload(asset)
  }

}

class DownloadListViewModel(context: Context): ViewModel() {

  private var tpStreamDownloadManager: TpStreamDownloadManager = TpStreamDownloadManager(context)

  fun getDownloadData(): LiveData<List<Asset>?> {
    return tpStreamDownloadManager.getAllDownloads()
  }

  fun pauseDownload(offlineAssetInfo: Asset) {
    tpStreamDownloadManager.pauseDownload(offlineAssetInfo)
  }

  fun resumeDownload(offlineAssetInfo: Asset) {
    tpStreamDownloadManager.resumeDownload(offlineAssetInfo)
  }

  fun cancelDownload(offlineAssetInfo: Asset) {
    tpStreamDownloadManager.cancelDownload(offlineAssetInfo)
  }

  fun deleteDownload(offlineAssetInfo: Asset) {
    tpStreamDownloadManager.deleteDownload(offlineAssetInfo)
  }

  companion object {
    fun init(fragmentActivity: FragmentActivity) : DownloadListViewModel {
      return ViewModelProvider(fragmentActivity, object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return DownloadListViewModel(fragmentActivity) as T
        }
      })[DownloadListViewModel::class.java]
    }
  }
}
