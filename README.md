# react-native-tpstreams

Video Component for TPStreams

## Installation

```sh
npm install react-native-tpstreams
```

### Initializing TPStreams SDK

First, import the package:

```javascript
import { NativeModules } from 'react-native';
const { Tpstreams } = NativeModules;
```

Next, initialize the SDK with your organization ID:

```javascript
Tpstreams.initializeTPSPlayer("YOUR_ORGANIZATION_ID");
```

Make sure to replace `YOUR_ORGANIZATION_ID` with your actual organization ID. This should be called at the entry point of your application to ensure proper initialization.

## Play a Video

To play a video using the TPStreams Player SDK, use the `TpStreamsPlayerView` component:

```javascript
import React, { useState } from 'react';
import { View, StyleSheet } from 'react-native';
import TpStreamsPlayerView from 'react-native-tpstreams';

const App = () => {
  const [playerProps] = useState({
    videoId: 'ASSET_ID',
    accessToken: 'ACCESS_TOKEN',
    enableDownload: false,
    autoPlay: true,
    startAt: 0,
    offlineLicenseExpireTime: 15,
    style: { width: '100%', height: 300 },
  });

  return (
    <View style={styles.container}>
      <TpStreamsPlayerView {...playerProps} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default App;
```

Replace `ASSET_ID` and `ACCESS_TOKEN` with the actual assetId and accessToken of the video you wish to play.

## Player Props
The player component accepts the following props:

| Prop                      | Type    | Required | Default | Description                                            |
|--------------------------|---------|----------|---------|--------------------------------------------------------|
| `videoId`                | string  | Yes      | -       | The unique identifier of the video asset.              |
| `accessToken`            | string  | Yes      | -       | The authentication token required to access the video. |
| `enableDownload`         | boolean | No       | `true`  | Enables or disables video download.                   |
| `autoPlay`               | boolean | No       | `true`  | Controls whether the video should start playing automatically. |
| `startAt`                | number  | No       | `0`     | Start the video from a particular time (in seconds).  |
| `offlineLicenseExpireTime` | number | No    | `1296000(15 days in seconds)`    | DRM license expiration time in seconds.                  |
| `style`                  | object  | No       | `{ width: '100%', height: 300 }` | Defines the player’s width and height. |

# Player Methods

To use the `Tpstreams` module, first import `NativeModules` from `react-native` and get the `Tpstreams` instance:

```js
import { NativeModules } from 'react-native';
const { Tpstreams } = NativeModules;
```

The `Tpstreams` module provides several methods to control video playback and manage player states. Below is a detailed explanation of each method:

## Play

```js
Tpstreams.play();
```

Starts video playback. Call this method when you want the video to start playing or resume after being paused.

## Pause

```js
Tpstreams.pause();
```

Pauses video playback, allowing it to be resumed later from the same position.

## Seek To

```js
Tpstreams.seekTo(position: number);
```

Seeks to a specific position in the video (in milliseconds).

**Example Usage:**

```js
Tpstreams.seekTo(10000); // Jump to the 10-second mark
```

## Get Current Time

```js
Tpstreams.getCurrentTime().then(console.log);
```

Gets the current playback position of the video (in milliseconds). Returns a promise that resolves with the current time.

## Get Duration

```js
Tpstreams.getDuration().then(console.log);
```

Retrieves the total duration of the currently loaded video (in milliseconds). Returns a promise that resolves with the total duration.

## Get Buffered Time

```js
Tpstreams.getBufferedTime().then(console.log);
```

Gets the amount of video data that has been buffered (in milliseconds). Returns a promise that resolves with the buffered time.

## Get Playback State

```js
Tpstreams.getPlaybackState().then(console.log);
```

Gets the current playback state of the player (e.g., `playing`, `paused`, `buffering`). Returns a promise that resolves with the state.

## Get Play When Ready

```js
Tpstreams.getPlayWhenReady().then(console.log);
```

Checks if the player is set to start playback automatically. Returns a promise that resolves with a boolean value.

## Set Play When Ready

```js
Tpstreams.setPlayWhenReady(true); // Enable autoplay
```

Controls whether the player should start playback automatically when loaded.

## Get Playback Speed

```js
Tpstreams.getPlaybackSpeed().then(console.log);
```

Gets the current playback speed of the video. Returns a promise that resolves with the speed value.

## Set Playback Speed

```js
Tpstreams.setPlaybackSpeed(1.5); // Play at 1.5x speed
```

Changes the playback speed of the video.

## Release

```js
Tpstreams.release();
```

Releases the player resources, stopping playback and hiding the player UI. Once called, the player cannot be used again unless reinitialized.


# Player Events

`Tpstreams` provides event listeners to handle various player events. Below are the available events and how to use them:

## Listening to Events

To subscribe to player events, use the `NativeEventEmitter` from `react-native` with `TpstreamsModule`:

```js
import { NativeEventEmitter, NativeModules } from 'react-native';

const { Tpstreams } = NativeModules;
const eventEmitter = new NativeEventEmitter(Tpstreams);

const subscription = eventEmitter.addListener('onPlaybackStateChanged', (state) => {
  console.log('Playback State Changed:', state);
});

// Remember to remove the listener when unmounting
subscription.remove();
```

## Available Events

### onPlaybackStateChanged
This event notifies us when the player's playback state changes:

| State | Value | Description |
|--------|-------|------------|
| `STATE_IDLE` | `1` | The player is idle and not ready to play. |
| `STATE_BUFFERING` | `2` | The player is buffering (waiting for enough data). |
| `STATE_READY` | `3` | The player is ready to play. |
| `STATE_ENDED` | `4` | The playback has finished. |


```js
eventEmitter.addListener('onPlaybackStateChanged', (state) => {
  console.log('Playback State:', state);
});
```

### onAccessTokenExpired
Triggered when the access token expires (requires refresh).

```js
eventEmitter.addListener('onAccessTokenExpired', async () => {
  console.log('Access token expired, fetching a new one...');
  
  const newToken = await fetchNewAccessToken(); // Fetch the new token from your API
  Tpstreams.setNewAccessToken(newToken);
});
```

### onMarkerCallback
Triggered when a marker (timestamp) is reached during playback.

```js
eventEmitter.addListener('onMarkerCallback', (marker) => {
  console.log('Marker reached:', marker);
});
```

### onDeviceInfoChanged
Triggered when the device information changes.

```js
eventEmitter.addListener('onDeviceInfoChanged', (info) => {
  console.log('Device info changed:', info);
});
```

### onFullScreenChanged
Triggered when the fullscreen mode changes.

```js
eventEmitter.addListener('onFullScreenChanged', (isFullscreen) => {
  console.log('Fullscreen mode:', isFullscreen);
});
```

### onIsLoadingChanged
- `true`: The player is currently loading or buffering new data from the network.
- `false`: The player has sufficient buffered content and can play without interruption.

```js
eventEmitter.addListener('onIsLoadingChanged', (isLoading) => {
  console.log('Loading state changed:', isLoading);
});
```

### onIsPlayingChanged
Triggered when the player’s playing state changes.

```js
eventEmitter.addListener('onIsPlayingChanged', (isPlaying) => {
  console.log('Playing state:', isPlaying);
});
```

### onPlayerError
Triggered when the player encounters an error.

```js
eventEmitter.addListener('onPlayerError', (error) => {
  console.log('Player Error:', error);
});
```

### onSeekBackIncrementChanged
Triggered when the seek-back increment value changes.

```js
eventEmitter.addListener('onSeekBackIncrementChanged', (value) => {
  console.log('Seek back increment changed:', value);
});
```

### onSeekForwardIncrementChanged
Triggered when the seek-forward increment value changes.

```js
eventEmitter.addListener('onSeekForwardIncrementChanged', (value) => {
  console.log('Seek forward increment changed:', value);
});
```

### onTimelineChanged
Triggered when the timeline of the player changes.

```js
eventEmitter.addListener('onTimelineChanged', (timeline) => {
  console.log('Timeline changed:', timeline);
});
```

### onTracksChanged
Triggered when the available tracks change (e.g., audio/video/subtitles).

```js
eventEmitter.addListener('onTracksChanged', (tracks) => {
  console.log('Tracks changed:', tracks);
});
```

# Download Module

The `Download Module` in `Tpstreams` allows users to manage offline video downloads efficiently. This includes observing download progress, pausing, resuming, canceling, and deleting downloads.

## Observing Download Data

To start observing the download data, call the `observeDownloadData` method from `FragmentModule`.

```js
import { NativeModules, DeviceEventEmitter } from 'react-native';

const { FragmentModule } = NativeModules;
FragmentModule.observeDownloadData();
```

Once initialized, the module will emit events whenever the download data changes.

## Listening to Download Events

To listen for download state changes, use `DeviceEventEmitter`:

```js
const subscription = DeviceEventEmitter.addListener(
  'onDownloadDataChanged',
  (event) => {
    console.log('Download data updated:', event.assets);
  }
);

// Remember to remove the listener when unmounting
subscription.remove();
```

## Managing Downloads

### Pause Download
Pauses an active download.

```js
FragmentModule.pauseDownload(videoId);
```

### Resume Download
Resumes a paused download.

```js
FragmentModule.resumeDownload(videoId);
```

### Cancel Download
Cancels an ongoing download.

```js
FragmentModule.cancelDownload(videoId);
```

### Delete Download
Deletes a completed download from storage.

```js
FragmentModule.deleteDownload(videoId);
```

## Rendering a Download List

Below is a sample implementation of a download list UI using `FlatList` in React Native:

```js
import React, { useState, useEffect } from 'react';
import { SafeAreaView, Text, FlatList, View, Button, DeviceEventEmitter } from 'react-native';
import { NativeModules } from 'react-native';

const { FragmentModule } = NativeModules;

const DownloadListScreen = () => {
  const [downloads, setDownloads] = useState([]);

  useEffect(() => {
    FragmentModule.observeDownloadData();
    const subscription = DeviceEventEmitter.addListener('onDownloadDataChanged', (event) => {
      setDownloads(event.assets);
    });
    return () => subscription.remove();
  }, []);

  const renderItem = ({ item }) => (
    <View>
      <Text>{item.title} - {item.percentage}%</Text>
      {item.status === 'DOWNLOADING' && <Button title="Pause" onPress={() => FragmentModule.pauseDownload(item.videoId)} />}
      {item.status === 'COMPLETE' && <Button title="Delete" onPress={() => FragmentModule.deleteDownload(item.videoId)} />}
      {item.status === 'PAUSED' && <Button title="Resume" onPress={() => FragmentModule.resumeDownload(item.videoId)} />}
    </View>
  );

  return (
    <SafeAreaView>
      <FlatList data={downloads} keyExtractor={(item) => item.videoId} renderItem={renderItem} />
    </SafeAreaView>
  );
};

export default DownloadListScreen;
```

This component observes download events and dynamically updates the list with buttons for user interactions.