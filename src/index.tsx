import Tpstreams from './NativeTpstreams';
import { requireNativeComponent } from 'react-native';
import type { PlayerProps, DRM, Source } from './types';

export function multiply(a: number, b: number): number {
  return Tpstreams.multiply(a, b);
}

const TpStreamsPlayerView = requireNativeComponent<PlayerProps>(
  'TpStreamsPlayerView'
);

export default TpStreamsPlayerView;
// Re-export types for external usage
export type { DRM, Source, PlayerProps };
