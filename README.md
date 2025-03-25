# react-native-tpstreams

Video Component for TPStreams

## Installation

```sh
npm install react-native-tpstreams
```

## Usage

```js
import React, { useState } from 'react';
import { View, Button, StyleSheet, NativeModules } from 'react-native';
import TpStreamsPlayerView from 'react-native-tpstreams';

const { Tpstreams } = NativeModules;

// Initialize the player with your organization UUID
Tpstreams.initializeTPSPlayer("ORGANIZATION_ID");

const App = () => {
  const [playerProps] = useState({
    videoId: 'ASSET_ID',
    accessToken: 'ACCESS_TOKEN',
    enableDownload: false,
    autoPlay: true,
    style: { width: '100%', height: 300 },
  });

  return (
    <View style={styles.container}>
      <TpStreamsPlayerView {...playerProps} />
      <View style={styles.buttonContainer}>
        <Button title="Play" onPress={() => Tpstreams.play()} />
        <Button title="Pause" onPress={() => Tpstreams.pause()} />
        <Button title="Seek to 10s" onPress={() => Tpstreams.seekTo(10000)} />
        <Button title="Get Current Time" onPress={() => Tpstreams.getCurrentTime().then(console.log)} />
        <Button title="Get Duration" onPress={() => Tpstreams.getDuration().then(console.log)} />
        <Button title="Get Playback Speed" onPress={() => Tpstreams.getPlaybackSpeed().then(console.log)} />
        <Button title="Set Speed 1.5x" onPress={() => Tpstreams.setPlaybackSpeed(1.5)} />
        <Button title="Set Speed 1x" onPress={() => Tpstreams.setPlaybackSpeed(1.0)} />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  buttonContainer: {
    marginTop: 20,
    width: '90%',
  },
});

export default App;
```


## Player Props

The player component accepts the following props:

### `videoId` (string, required)
The unique identifier of the video asset to be played.

### `accessToken` (string, required)
The authentication token required to access the video.

### `enableDownload` (boolean, optional, default: `true`)
Determines whether the video can be downloaded.  
- `true` → Enables download functionality (default).  
- `false` → Disables downloads.

### `autoPlay` (boolean, optional, default: `true`)
Controls whether the video should start playing automatically when loaded.  
- `true` → Video starts playing automatically (default).  
- `false` → User must manually start playback.

### `style` (object, optional)
Defines the player’s width and height.  
Example:  
```js
style: { width: 600, height: 300 }
```
## Player Methods

### `play()`
Starts video playback from the current position.

### `pause()`
Pauses video playback, allowing it to be resumed later.

### `seekTo(position: Double)`
Seeks to a specific position in the video (in milliseconds).  
For example, `seekTo(10000)` moves the playback to 10 seconds.

### `getCurrentTime(promise: Promise)`
Gets the current playback time (in milliseconds).  
Returns a promise that resolves with the current position of the video.

### `getDuration(promise: Promise)`
Retrieves the total duration of the video (in milliseconds).  
Returns a promise that resolves with the length of the video.

### `getBufferedTime(promise: Promise)`
Gets the amount of video data that has been buffered (in milliseconds).  
Returns a promise with the buffered time to help manage network-based playback issues.

### `getPlaybackState(promise: Promise)`
Gets the current playback state of the player (e.g., playing, paused, buffering).  
Returns a promise that resolves with a state value.

### `getPlayWhenReady(promise: Promise)`
Checks whether the player is set to start playback automatically.  
Returns a boolean promise (`true` if autoplay is enabled).

### `setPlayWhenReady(playWhenReady: Boolean)`
Controls whether the player should automatically start playback when loaded.  
Pass `true` to enable autoplay or `false` to disable it.

### `getPlaybackSpeed(promise: Promise)`
Gets the current playback speed of the video.  
Returns a promise resolving with the playback speed (e.g., `1.0` for normal speed, `1.5` for 1.5x speed).

### `setPlaybackSpeed(speed: Float)`
Changes the playback speed of the video.  
For example, `setPlaybackSpeed(2.0)` doubles the speed, while `setPlaybackSpeed(0.5)` slows it down.

### `release()`
Releases the player resources, stopping playback and hiding the player UI.  
Once called, the player cannot be used again unless reinitialized.


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
