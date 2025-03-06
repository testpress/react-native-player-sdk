import { View, Text } from 'react-native';
import TpStreamsPlayerView from 'react-native-tpstreams';
import type { PlayerProps } from 'react-native-tpstreams';

const App = () => {
  const playerProps: PlayerProps = {
    source: {
      uri: 'https://example.com/video.mp4',
      drm: {
        type: 'widevine',
        licenseServer: 'https://example.com/license',
        headers: {
          Authorization: 'Bearer your_token',
        },
      },
    },
    style: { width: '100%', height: 200 },
  };

  return (
    <View
      style={{
        flex: 1,
        justifyContent: 'flex-start',
        alignItems: 'center',
        paddingTop: 20,
      }}
    >
      <Text
        style={{
          fontSize: 18,
          fontWeight: 'bold',
        }}
      >
        Example Player
      </Text>
      <TpStreamsPlayerView {...playerProps} />
    </View>
  );
};

export default App;
