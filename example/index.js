import { AppRegistry, NativeModules } from 'react-native';
import App from './src/App';
import { name as appName } from './app.json';

const { Tpstreams } = NativeModules;
Tpstreams.initializeTPSPlayer("6eafqn");

AppRegistry.registerComponent(appName, () => App);
