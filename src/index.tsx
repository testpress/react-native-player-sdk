import Tpstreams from './NativeTpstreams';
import type { PlayerProps, DRM, Source } from './types';

export function multiply(a: number, b: number): number {
  return Tpstreams.multiply(a, b);
}

// Re-export types for external usage
export type { DRM, Source, PlayerProps };
