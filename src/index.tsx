// import Tpstreams from './NativeTpstreams';

import type { PlayerProps, DRM, Source, TpStreamsPlayerProps } from './types';
import { requireNativeComponent } from 'react-native';

const TpStreamsPlayerView = requireNativeComponent<TpStreamsPlayerProps>(
  'TpStreamsPlayerView'
);

export default TpStreamsPlayerView;
export type { DRM, Source, PlayerProps, TpStreamsPlayerProps };
