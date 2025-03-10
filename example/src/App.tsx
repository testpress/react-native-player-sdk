import { View, Text, StyleSheet } from 'react-native';
import TpStreamsPlayerView from 'react-native-tpstreams';
import type { TpStreamsPlayerProps } from 'react-native-tpstreams';

const App = () => {
  const playerProps: TpStreamsPlayerProps = {
    videoId: '68PAFnYTjSU',
    accessToken: '5f3ded52-ace8-487e-809c-10de895872d6',
    style: { width: '100%', height: 300 },
  };

  return (
    <View style={styles.container}>
      <View style={styles.playerContainer}>
        <TpStreamsPlayerView {...playerProps} />
      </View>
      <Text style={styles.text}>Tp Streams⚡</Text>
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
});

export default App;
