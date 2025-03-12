# react-native-tpstreams

Video Component for TPStreams

## Installation

```sh
npm install react-native-tpstreams
```

## Usage


```js
import React, { useState } from 'react';
import { NativeModules } from 'react-native';
import TpStreamsPlayerView from 'react-native-tpstreams';

const { Tpstreams } = NativeModules;

// Initialize the player with your organization UUID
Tpstreams.initializeTPSPlayer("ORGANIZATION_ID");

const App = () => {
  const [playerProps, setPlayerProps] = useState({
    videoId: 'ASSET_ID',
    accessToken: 'ACCESS_TOKEN',
    style: { width: '100%', height: 300 },
  });

  return <TpStreamsPlayerView {...playerProps} />;
};

export default App;

```


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
