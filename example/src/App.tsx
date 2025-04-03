import { View, Text, StyleSheet, Button } from 'react-native';
import { useState } from 'react';
import TpStreamsPlayerView from 'react-native-tpstreams';
import type { TpStreamsPlayerProps } from 'react-native-tpstreams';

const App = () => {
  const [showPlayer, setShowPlayer] = useState(true); // Toggle visibility

  const [playerProps, setPlayerProps] = useState<TpStreamsPlayerProps>({
    videoId: '95hBGFAhQYR',
    accessToken: '7d4e2ffb-3492-4cd4-8e5c-41b7af2f3e7f',
    style: { width: '100%', height: 300 },
  });

  const handleDrmVideo = () => {
    setPlayerProps({
      videoId: 'YtuNKqjgK9D',
      accessToken: 'fd591e6b-be1d-4703-929a-a6188540cfed',
      enableDownload: true,
      autoPlay: true,
      style: { width: '100%', height: 300 },
    });
    setShowPlayer(true); // Ensure player is shown
  };

  const handleNonDrmVideo = () => {
    setPlayerProps({
      videoId: '8yCHhR2CY6t',
      accessToken: 'e55805b7-84a8-4270-bf21-bdd6f1d346af',
      enableDownload: false,
      autoPlay: false,
      style: { width: '100%', height: 300 },
    });
    setShowPlayer(true);
  };

  return (
    <View style={styles.container}>
      <View style={styles.playerContainer}>
        {showPlayer && <TpStreamsPlayerView {...playerProps} />}
      </View>
      <Text style={styles.text}>Tp Streams⚡</Text>
      <View style={styles.buttonContainer}>
        <Button title="DRM Video" onPress={handleDrmVideo} />
        <Button title="Non-DRM Video" onPress={handleNonDrmVideo} />
        <Button
          title={showPlayer ? 'Remove Player' : 'Show Player'}
          onPress={() => setShowPlayer(!showPlayer)}
          color="red"
        />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    justifyContent: 'center',
    alignItems: 'center',
  },
  playerContainer: {
    flex: 1,
    width: '100%',
    height: 300,
    alignItems: 'center',
  },
  text: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 50,
    marginLeft: 10,
    marginRight: 10,
    gap: 10,
  },
});
export default App;
