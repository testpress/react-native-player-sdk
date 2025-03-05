export type DRM = {
  type: 'widevine' | 'playready' | 'fairplay';
  licenseServer: string;
  headers?: Record<string, string>;
  certificateUrl?: string;
  base64Certificate?: boolean;
  persistDrmSession?: boolean;
};

export type Source = {
  uri: string;
  drm?: DRM;
};

export type PlayerProps = {
  source: Source;
  style?: import('react-native').ViewStyle;
};
