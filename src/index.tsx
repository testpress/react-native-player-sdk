import type { TpStreamsPlayerProps } from './types';
import { requireNativeComponent } from 'react-native';

const TpStreamsPlayerView = requireNativeComponent<TpStreamsPlayerProps>(
  'TpStreamsPlayerView'
);

export default TpStreamsPlayerView;
export type { TpStreamsPlayerProps };
